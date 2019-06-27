package org.minioasis.library.domain.search;

import java.io.Serializable;

import org.minioasis.library.domain.JournalEntry;
import org.minioasis.library.domain.JournalEntryLine;

public class JournalEntryDTO implements Serializable {

	private static final long serialVersionUID = 4399338217192413674L;
	
	private String pid;
	private JournalEntry je = new JournalEntry();
	private JournalEntryLine line;
	private JournalEntry done = null;

	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public JournalEntry getJe() {
		return je;
	}
	public void setJe(JournalEntry je) {
		this.je = je;
	}
	public JournalEntryLine getLine() {
		return line;
	}
	public void setLine(JournalEntryLine line) {
		this.line = line;
	}
	public JournalEntry getDone() {
		return done;
	}
	public void setDone(JournalEntry done) {
		this.done = done;
	}
	public void addLines() {
		if(line != null) {
			je.addLine(line);
			line = new JournalEntryLine();
		}
	}
	public void removeLines() {
		je.removeLines();
	}

}
