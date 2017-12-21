package faceduck.skeleton.util;

import static faceduck.skeleton.util.Direction.EAST;
import static faceduck.skeleton.util.Direction.NORTH;
import static faceduck.skeleton.util.Direction.SOUTH;
import static faceduck.skeleton.util.Direction.WEST;

import java.util.Random;

import faceduck.skeleton.interfaces.World;

/**
 * Useful utilities, currently focused on randomization. We use a fixed seed so
 * that simulations are repeatable.
 */
public class Util {

	private static final int NUM_DIRECTIONS = Direction.values().length;
	private static final Random rand = new Random(2013);

	/**
	 * Returns a random Direction.
	 */
	public static Direction randomDir() {
		switch (rand.nextInt(NUM_DIRECTIONS)) {
		case 0:
			return NORTH;
		case 1:
			return SOUTH;
		case 2:
			return EAST;
		case 3:
			return WEST;
		}
		throw new RuntimeException("Impossible to get here.");
	}
	
//	/**
//	 * Returns a random, vacant Direction using randomDir().
//	 *
//	 * @param world
//	 *            The world to search.
//	 * @param loc
//	 *        	  location.
//	 *
//	 * @return A random vacant direction, or null if the adjacent is full.
//	 */
//	public static Direction getVacantDir(World world, Location loc) {
//		if (world == null){
//			throw new NullPointerException("World cannot be null.");
//		} else if (loc == null) {
//			throw new NullPointerException("Location cannot be null.");
//		}
//		
//		while (true) {
//			Direction dir = Util.randomDir();
//			// seed is consistent, so this might result same even if random
//			Location dest = new Location(loc, dir);
//			if (world.isValidLocation(dest)) {
//				if (world.getThing(dest) == null) {
//					return dir;
//				}
//				// if loc is surrounded by objects in every direction
//				else if (!Util.abletoMove(world,loc)) {
//					System.out.println("Nowhere to move, check other direction");
//					break;
//				}
//			}
//		}
//		// if every direction is surrounded
//		return null;
//	}
//	
//	/**
//	 * Checks location's status if location's adjacent directions are valid
//	 * and empty.
//	 * 
//	 * @param world
//	 *            The world to search.
//	 * @param loc
//	 *        	  location.
//	 *
//	 * @return true if it is able to move.
//	 */
//	public static boolean abletoMove(World world, Location loc) {
//		
//		final Location locEast = new Location(loc, Direction.EAST);
//		final Location locWest = new Location(loc, Direction.WEST);
//		final Location locNorth = new Location(loc, Direction.NORTH);
//		final Location locSouth = new Location(loc, Direction.SOUTH);
//		
//		// When East is not valid
//		if(!world.isValidLocation(locEast)){
//			// East & North is not valid
//			if(!world.isValidLocation(locNorth)){
//				if(world.getThing(locWest) != null && world.getThing(locSouth) != null)
//					return false;
//			}
//			// East & South is not valid
//			else if(!world.isValidLocation(locSouth)){
//				if(world.getThing(locWest) != null && world.getThing(locNorth) != null)
//					return false;
//			}
//			// only East is not valid
//			else{
//				if(world.getThing(locWest) != null && world.getThing(locSouth) != null 
//						&& world.getThing(locNorth) != null)
//					return false;
//			}
//		}
//		// When West is not valid
//		else if(!world.isValidLocation(locWest)){
//			// West & North is not valid
//			if(!world.isValidLocation(locNorth)){
//				if(world.getThing(locEast) != null && world.getThing(locSouth) != null)
//					return false;
//			}
//			// West & South is not valid
//			else if(!world.isValidLocation(locSouth)){
//				if(world.getThing(locEast) != null && world.getThing(locNorth) != null)
//					return false;
//			}
//			// only West is not valid
//			else{
//				if(world.getThing(locEast) != null && world.getThing(locSouth) != null 
//						&& world.getThing(locNorth) != null)
//					return false;
//			}
//		}
//		// When North is not valid
//		else if (!world.isValidLocation(locNorth)) {
//			if (world.getThing(locEast) != null && world.getThing(locSouth) != null && world.getThing(locWest) != null)
//				return false;
//		}
//		// When South is not valid
//		else if (!world.isValidLocation(locSouth)) {
//			if (world.getThing(locEast) != null && world.getThing(locNorth) != null && world.getThing(locWest) != null)
//				return false;
//		}
//		// When every direction is valid, but surrounded by objects in every direction
//		else if(world.getThing(locEast) != null && world.getThing(locWest) != null &&
//				world.getThing(locSouth) != null && world.getThing(locNorth) != null){
//			return false;
//		}
//		
//		// after all conditional statements, this loc is able to move
//		return true;
//	}
	
	/**
	 * Finds a random empty location in world.
	 *
	 * @param world
	 *            The world to search.
	 *
	 * @return A random location, or null if the world is full.
	 */
	public static Location randomEmptyLoc(World world) {
		return randomEmptyLoc(world, 0, 0, world.getWidth(), world.getHeight());
	}

	/**
	 * Finds a random empty location in the world within a bounding box.
	 *
	 * @return A random location, or null if the world is full.
	 */
	public static Location randomEmptyLoc(World w, int startW, int startH,
			int width, int height) {
		int x = rand.nextInt(width - startW) + startW;
		int y = rand.nextInt(height - startH) + startH;
		
		// first position is random, if full searches sequentially
		for (int i = startW; i < width; ++i) {
			x = ((x + 1) % (width - startW)) + startW;
			for (int j = startH; j < height; ++j) {
				y = ((y + 1) % (height - startH)) + startH;
				Location loc = new Location(x, y);
				if (w.getThing(loc) == null) {
					return loc;
				}
			}
		}
		// no position free
		return null;
	}
}
