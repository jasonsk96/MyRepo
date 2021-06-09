public class Entrance{
	private int i;
	private int j;
	private Hotel hotel = null;
	
	Entrance(Square ps){
		i = ps.getI();
		j = ps.getJ();
	}
	
	/**
	 * @return entrance square's row position on the game board
	 */
	public int getI(){
		return i;
	}
	
	/**
	 * @return entrance square's column position on the game board
	 */
	public int getJ(){
		return j;
	}
	
	/**
	 * @param hotelName passed and set as the hotel which this entrance is bought for 
	 */
	public void setHotel(Hotel hotelName){
		hotel = hotelName;
	}
	
	/**
	 * @return hotel which this entrance is bought for
	 */
	public Hotel getHotel(){
		return hotel;
	}
}