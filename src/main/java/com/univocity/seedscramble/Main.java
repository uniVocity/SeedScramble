package com.univocity.seedscramble;

import com.github.weisj.darklaf.*;
import com.github.weisj.darklaf.components.*;
import com.github.weisj.darklaf.components.border.*;
import com.github.weisj.darklaf.theme.*;
import com.univocity.seedscramble.seed.*;
import com.univocity.seedscramble.wordlist.*;
import org.slf4j.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.nimbus.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;

public class Main extends JFrame {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	static boolean themeManagerLoaded = false;
	private static Theme defaultTheme;

	private static void initUI() {
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");

		Locale.setDefault(new Locale("en", "US"));
		System.setProperty("user.language", "en");
		System.setProperty("user.country", "US");

		try {
			defaultTheme = new OneDarkTheme();
			LafManager.install(defaultTheme);
			LafManager.enabledPreferenceChangeReporting(true);

			Theme[] themes = LafManager.getRegisteredThemes();
			for (Theme theme : themes) {
				if (theme.getName().toLowerCase().contains("intellij")) {
					LafManager.replaceTheme(theme, theme.withDisplayName("Default light"));
					break;
				}
			}
			themeManagerLoaded = true;
		} catch (Exception e) {
			log.warn("Error initializing darcula theme");
			try {
				UIManager.setLookAndFeel(new NimbusLookAndFeel());
			} catch (Exception ex) {
				log.warn("Error initializing looking and feel", e);
			}
		}

		boolean isMacOs = (System.getProperty("os.name").toLowerCase().contains("mac"));
		if (isMacOs) {
			InputMap im = (InputMap) UIManager.get("TextField.focusInputMap");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), DefaultEditorKit.copyAction);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), DefaultEditorKit.pasteAction);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), DefaultEditorKit.cutAction);
		}
	}

	private SeedPhraseInput seedInput;
	private JTextArea scrambledOutput;
	private JSpinner magicNumber;
	private JTextField password;
	private JButton btScramble;
	private JButton btUnscramble;
	private JPanel centralPanel;

	public Main() {
		super("Seed Scramble");

		URL icon = Main.class.getResource("/images/icon128x128.png");
		if(icon != null) {
			setIconImage(new ImageIcon(icon).getImage());
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 600);
		setLocationRelativeTo(null);

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(addBorder("Input seed", getSeedInput()), c);

		c.gridy = 1;
		c.weighty = 0;
		add(getCentralPanel(), c);

		c.gridy = 2;
		c.weighty = 1;
		add(addBorder("Output", getScrambledOutput()), c);

		c.weighty = 0;
		c.weightx = 1;
		c.gridy = 3;
		add(addBorder("", new JLabel("<html>Brought to you by the [<b>SHOP</b>] stake pool</html>", SwingConstants.RIGHT)), c);
	}

	private JPanel getCentralPanel() {
		if (centralPanel == null) {
			centralPanel = new JPanel(new GridBagLayout());
			centralPanel.setBorder(DarkBorders.createLineBorder(1, 1, 1, 1));

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.fill = GridBagConstraints.HORIZONTAL;

			c.gridx = 0;
			centralPanel.add(new JLabel("Password"), c);

			c.gridx = 2;
			centralPanel.add(new JLabel("Magic number"), c);

			c.gridx = 1;
			c.weightx = 1;
			centralPanel.add(getPassword(), c);

			c.gridx = 3;
			c.weightx = 0;
			centralPanel.add(getMagicNumber(), c);

			c.gridx = 4;
			centralPanel.add(getBtScramble(), c);

			c.gridx = 5;
			centralPanel.add(getBtUnscramble(), c);
		}
		return centralPanel;
	}

	public SeedPhraseInput getSeedInput() {
		if (seedInput == null) {
			seedInput = new SeedPhraseInput(Seed.englishMnemonicWords());
		}
		return seedInput;
	}

	public JTextArea getScrambledOutput() {
		if (scrambledOutput == null) {
			scrambledOutput = new JTextArea();
			scrambledOutput.setEditable(false);
			scrambledOutput.setWrapStyleWord(true);
			scrambledOutput.setLineWrap(true);
		}
		return scrambledOutput;
	}

	public JSpinner getMagicNumber() {
		if (magicNumber == null) {
			magicNumber = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		}
		return magicNumber;
	}

	public JTextField getPassword() {
		if (password == null) {
			password = new JTextField();
		}
		return password;
	}

	public JButton getBtScramble() {
		if (btScramble == null) {
			btScramble = new JButton("Scramble");
			btScramble.addActionListener(e -> scramble());
		}
		return btScramble;
	}


	public JButton getBtUnscramble() {
		if (btUnscramble == null) {
			btUnscramble = new JButton("Unscramble");
			btUnscramble.addActionListener(e -> unscramble());
		}
		return btUnscramble;
	}

	private void scramble() {
		try {
			String scrambled = SeedScrambler.scrambleSeed(getSeedInput().getText(), (Integer) getMagicNumber().getValue(), getPassword().getText());
			getScrambledOutput().setText(scrambled);
		} catch (Exception e) {
			log.error("Error scrambling seed", e);
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void unscramble() {
		try {
			String unscrambled = SeedScrambler.unscrambleSeed(getSeedInput().getText(), (Integer) getMagicNumber().getValue(), getPassword().getText());
			getScrambledOutput().setText(unscrambled);
		} catch (Exception e) {
			log.error("Error unscrambling seed", e);
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JPanel addBorder(String title, JComponent component) {
		JPanel out = new JPanel(new BorderLayout());
		out.add(new OverlayScrollPane(new JScrollPane(component)), BorderLayout.CENTER);
		out.setBorder(new TitledBorder(DarkBorders.createTopBorder(), title));
		return out;
	}

	public static void main(String... args) {
		initUI();
		Main main = new Main();
		SwingUtilities.invokeLater(() -> main.setVisible(true));
	}
}
