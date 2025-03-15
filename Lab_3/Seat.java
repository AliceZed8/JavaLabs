package Lab_3;

public class Seat implements Comparable<Seat> {
	private Integer rowNum, colNum;
	private Boolean isOccupied;

	public Seat(Integer rowNum, Integer colNum) {
		this.rowNum = rowNum;
		this.colNum = colNum;
		isOccupied = false;
	}
		
	public Boolean isOccupied() {
		return this.isOccupied;
	}

	public void setOccupied(Boolean value) {
		isOccupied = value;
	}

	public Integer getRowNum() {
		return rowNum;
	} 
	public Integer getColNum() {
		return colNum;
	}

	public Seat copy() {
		Seat seat = new Seat(rowNum, colNum);
		return seat;
	}

	@Override
	public int compareTo(Seat other) {
		if (this.rowNum != other.rowNum) return Integer.compare(this.rowNum, other.rowNum);
		return Integer.compare(this.colNum, other.colNum);
	}
}
