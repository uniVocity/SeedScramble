package com.univocity.seedscramble.wordlist;

import com.univocity.seedscramble.seed.*;
import org.apache.commons.lang3.*;
import org.slf4j.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.function.*;

public class SeedPhraseInput extends NoClipboardTextArea {
	private static final Logger log = LoggerFactory.getLogger(SeedPhraseInput.class);

	private WordListPopup popupMenu;
	private int expectedWordCount = -1;
	private Consumer<InvalidMnemonicException> invalidMnemonicHandler;

	public SeedPhraseInput(Collection<String> seedWords) {
		super(4, 32);
		this.popupMenu = new WordListPopup(this, seedWords);
		setWrapStyleWord(true);
		setLineWrap(true);
		setAutoscrolls(false);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_SPACE || e.getKeyChar() == KeyEvent.VK_ENTER || e.getKeyChar() == KeyEvent.VK_TAB) {
					if (popupMenu.insertSelection()) {
						if (e.getKeyChar() != KeyEvent.VK_SPACE) {
							e.consume();
						}
						final int position = getCaretPosition();
						SwingUtilities.invokeLater(() -> {
							try {
								getDocument().remove(position - 1, 1);
							} catch (BadLocationException ex) {
								log.error("Error processing seed word auto-completion", ex);
							}
						});
					}
				}

				SwingUtilities.invokeLater(() -> validateWords());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					showSuggestions();
				} else if (Character.isWhitespace(e.getKeyChar())) {
					popupMenu.setVisible(false);
				} else if (popupMenu.isVisible()) {
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						popupMenu.moveDown();
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						popupMenu.moveUp();
					}
				}
			}
		});


		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(popupMenu.isVisible()) {
					requestFocus();
				}
			}
		});
	}

	void validateWords() {
		if(invalidMnemonicHandler != null) {
			try {
				Seed.toValidatedMnemonicList(getText(), expectedWordCount);
				invalidMnemonicHandler.accept(null);
			} catch (InvalidMnemonicException e) {
				invalidMnemonicHandler.accept(e);
			}
		}
	}

	void insertString(int offset, String str) {
		try {
			getDocument().insertString(offset, str.trim() + " ", null);
		} catch (BadLocationException ex) {
			log.error("Error processing seed word auto-completion", ex);
		}
	}

	protected void showSuggestions() {
		SwingUtilities.invokeLater(this::displaySuggestions);
	}

	private void displaySuggestions() {
		popupMenu.setVisible(false);

		final int position = getCaretPosition();
		Rectangle location;
		try {
			location = modelToView(position).getBounds();
		} catch (BadLocationException e) {
			log.error("Error displaying seed word suggestion list", e);
			return;
		}
		String text = getText();
		int start = Math.max(0, position - 1);
		while (start > 0) {
			if (!Character.isWhitespace(text.charAt(start))) {
				start--;
			} else {
				start++;
				break;
			}
		}
		if (start > position) {
			return;
		}

		String wordFragment = text.substring(start, position);
		if (StringUtils.isBlank(wordFragment)) {
			return;
		}
		popupMenu.show(position, wordFragment, location);

		SwingUtilities.invokeLater(this::requestFocusInWindow);
	}

	public void setExpectedWordCount(int wordCount) {
		this.expectedWordCount = wordCount;
	}

	public int getExpectedWordCount() {
		return expectedWordCount;
	}

	public void setInvalidMnemonicHandler(Consumer<InvalidMnemonicException> invalidMnemonicHandler) {
		this.invalidMnemonicHandler = invalidMnemonicHandler;
	}

}
