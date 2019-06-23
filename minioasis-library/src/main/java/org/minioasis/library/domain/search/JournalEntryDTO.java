package org.minioasis.library.domain.search;

import java.io.Serializable;

import org.minioasis.library.domain.JournalEntry;
import org.minioasis.library.domain.JournalEntryLine;

public class JournalEntryDTO implements Serializable {

	private static final long serialVersionUID = 4399338217192413674L;
	
	private JournalEntry je = new JournalEntry();
	private JournalEntryLine line1 = new JournalEntryLine();
	private JournalEntryLine line2 = new JournalEntryLine();
	private JournalEntryLine line3 = new JournalEntryLine();

	public JournalEntry getJe() {
		return je;
	}
	public void setJe(JournalEntry je) {
		this.je = je;
	}
	public JournalEntryLine getLine1() {
		return line1;
	}
	public void setLine1(JournalEntryLine line1) {
		this.line1 = line1;
	}
	public JournalEntryLine getLine2() {
		return line2;
	}
	public void setLine2(JournalEntryLine line2) {
		this.line2 = line2;
	}
	public JournalEntryLine getLine3() {
		return line3;
	}
	public void setLine3(JournalEntryLine line3) {
		this.line3 = line3;
	}
	
	public void addLines() {
		if(line1.getDescription() != null 
				&& (line1.getAccount() != null || line1.getToAccount() != null) 
				&& (line1.getCredit().doubleValue() > 0 || line2.getDebit().doubleValue() > 0)) {
			je.addLine(line1);
		}
		if(line2.getDescription() != null 
				&& (line2.getAccount() != null || line2.getToAccount() != null) 
				&& (line2.getCredit().doubleValue() > 0 || line2.getDebit().doubleValue() > 0)) {
			je.addLine(line2);
		}
		if(line3.getDescription() != null 
				&& (line3.getAccount() != null || line3.getToAccount() != null) 
				&& (line3.getCredit().doubleValue() > 0 || line3.getDebit().doubleValue() > 0)) {
			je.addLine(line3);
		}
	}

}
