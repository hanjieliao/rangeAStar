package com.whg.myAStar;

import java.awt.Point;
import java.awt.Rectangle;

public class TileCell {

	public static final int WIDTH = 50;
	public static final int HEIGHT = 48;
	
	/** 实际的屏幕坐标 */
	private final Point point;
	
	/** 地图方格的坐标，即地图二维数组的下标 */
	private final Point cellPoint;

	private final TileCellType type;
	private double cost;
	private double g = -1;
	private double h;
	
	private boolean isEnd;
	private boolean isShow;
	private boolean isPath;

	public TileCell(int x, int y) {
		this(x, y, TileCellType.road);
	}
	
	public TileCell(int x, int y, TileCellType type) {
		this.point = new Point(x, y);
		this.cellPoint = transformCellPoint(point);
		this.type = type;
		this.cost = type.cost();
	}
	
//	public boolean isNormal(){
//		return type == TileCellType.NORMAL;
//	}
	
	public boolean isCanPass(){
		return type == TileCellType.road
			|| type == TileCellType.forest
			|| type == TileCellType.shoal
			|| type == TileCellType.commonResource
			|| type == TileCellType.centerResource
			|| type == TileCellType.camp;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public double getF(){
		return g + h;
	}
	
	public double getCost(){
		return cost;
	}

	public double getG() {
		return g>9 ? Math.round(g*10)/10.0 : Math.round(g*100)/100.0;
	}

	public void setG(double g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}
	
	public Point getPosition(){
		return point;
	}
	
	public Point getCellPosition(){
		return cellPoint;
	}
	
	public static Point transformCellPoint(Point point){
		return new Point((int)point.x/WIDTH, (int)point.y/HEIGHT);
	}
	
	/**
	 * 四舍五入去获取临近的方块来表示起点，
	 * 不四舍五入的话起点的选取在方块偏右下角时会往回走，
	 * 但这个问题在添加路径时不把起点加入进路径也可以解决 
	 */
	public static Point transformCellPointRound(Point point){
		return new Point(((int)Math.round(1.0*point.x/WIDTH)), (int)Math.round(1.0*point.y/HEIGHT));
	}

	public int getX() {
		return (int)point.getX();
	}

	public int getY() {
		return (int)point.getY();
	}
	
	/**  是否是起始/开始方块 */
	public boolean isStart() {
		return g == 0;
	}

	/**  是否是目标/结束方块 */
	public boolean isEnd() {
		return isEnd;
	}
	
	public void notEnd(){
		isEnd = false;
	}

	/** 是否显示G值 */
	public boolean isShow() {
		return isShow;
	}

	/** 显示G值 */
	public void show() {
		this.isShow = true;
	}
	
	public void notShow(){
		this.isShow = false;
	}
	
	public void notPath(){
		this.isPath = false;
	}
	
	/** 是否显示路径 */
	public boolean isPath(){
		return isPath;
	}
	
	public void markStart() {
		g = 0;
	}
	
	public void markEnd() {
		isEnd = true;
	}
	
	public void markPath() {
		isPath = true;
	}
	
	/** 设置此地图板块到起点的距离值 */
	public void addG(TileCell cell) {
		Point p = getCellPosition();
		double cellG = cell.getG();
		if(cell.type == TileCellType.road && type == TileCellType.shoal){
			cellG += 1; //道路到浅滩的消耗加1
		}

		//System.out.println(toString()+" 1-g="+getG()+",cellG="+cellG);
		if (g == -1) { //初始化，完毕后return，后续的都是保持距离为最小值
			g = cellG + cost;
			//System.out.println("init~"+p+",g="+g);
		}else if (cellG + cost < g) { //保持距离值为最小的
			g = cellG + cost;
			//System.out.println("update~"+p+",g="+g);
		}
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(getX(), getY(), WIDTH, HEIGHT);
	}
	
	public TileCellType type(){
		return type;
	}
	
	@Override
	public String toString(){
		return "(x="+getX()+", y="+getY()+")"
			+" [x="+(int)cellPoint.getX()+", y="+(int)cellPoint.getY()+"]"
			+" {cost="+cost+", g="+getG()+", h="+h+", f="+getF()+"}"
			+" isShow="+isShow;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileCell other = (TileCell) obj;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}

	public boolean isType(TileCellType type) {
		return this.type == type;
	}

//	public void reset() {
//		this.cost = NORMAL;
//		this.g = -1;
//	}

}
