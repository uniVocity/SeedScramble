package com.univocity.seedscramble.wordlist;

import com.github.weisj.darklaf.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class WordListPopup extends JPopupMenu {

	private SeedPhraseInput textComponent;
	private final WordList list;
	private String wordFragment;
	private int insertionPosition;

	public WordListPopup(SeedPhraseInput textComponent, Collection<String> words) {
		this(textComponent, words.toArray(new String[0]));
	}

	public WordListPopup(SeedPhraseInput textComponent, String... words) {
		this.textComponent = textComponent;
		this.list = new WordList(words);
		removeAll();
		setOpaque(false);
		setBorder(null);
		setPreferredSize(new Dimension(150, 200));

		OverlayScrollPane scroll = new OverlayScrollPane(new JScrollPane(list));
		scroll.setBorder(null);
		add(scroll, BorderLayout.CENTER);

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				insertSelection();
			}
		});
	}

	public void moveUp() {
		int index = Math.min(list.getSelectedIndex() - 1, 0);
		selectIndex(index);
	}

	public void moveDown() {
		int index = Math.min(list.getSelectedIndex() + 1, list.getModel().getSize() - 1);
		selectIndex(index);
	}

	private void selectIndex(int index) {
		final int position = textComponent.getCaretPosition();
		list.setSelectedIndex(index);
		SwingUtilities.invokeLater(() -> textComponent.setCaretPosition(position));
	}

	public void show(int position, String wordFragment, Rectangle location) {
		if (list.refreshWordList(wordFragment)) {
			this.insertionPosition = position;
			this.wordFragment = wordFragment;
			show(textComponent, location.x, textComponent.getBaseline(0, 0) + location.y);
		}
	}

	public boolean insertSelection() {
		if (isVisible() && list.getSelectedItem() != null) {
			String selectedSuggestion = list.getSelectedItem().substring(wordFragment.length());
			textComponent.insertString(insertionPosition, selectedSuggestion);
			setVisible(false);
			textComponent.validateWords();
			return true;
		}
		return false;
	}
}