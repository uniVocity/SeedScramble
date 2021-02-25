package com.univocity.seedscramble.wordlist;

import javax.swing.*;
import javax.swing.text.*;

public class NoClipboardTextArea extends JTextArea {

	{
		setTransferHandler(null);
		setComponentPopupMenu(null);
		setInheritsPopupMenu(false);
	}

	public NoClipboardTextArea() {
	}

	public NoClipboardTextArea(String text) {
		super(text);
	}

	public NoClipboardTextArea(int rows, int columns) {
		super(rows, columns);
	}

	public NoClipboardTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
	}

	public NoClipboardTextArea(Document doc) {
		super(doc);
	}

	public NoClipboardTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
	}

	@Override
	public void copy() {
	}
	@Override
	public void paste() {
	}
	@Override
	public void cut() {
	}
}
