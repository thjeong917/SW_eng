package faceduck.ai;

import static faceduck.skeleton.util.Direction.EAST;
import static faceduck.skeleton.util.Direction.NORTH;
import static faceduck.skeleton.util.Direction.SOUTH;
import static faceduck.skeleton.util.Direction.WEST;

import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;
import faceduck.skeleton.util.Util;

public abstract class AbstractAI implements AI {

	/**
	 * constructor for AbstractAI
	 */
	public AbstractAI() {
	}

	/**
	 * abstract act command
	 */
	public abstract Command act(World world, Actor actor);

	/**
	 * finds target in adjacent locations, and return that direction.
	 * 
	 * @return returns direction where target is located adjacent. if not,
	 *         returns null
	 */
	public Direction findPrey(World world, Location loc, Object target) {
		if (world == null) {
			throw new NullPointerException("World cannot be null.");
		} else if (loc == null) {
			throw new NullPointerException("Location cannot be null.");
		}

		boolean Eflag = false;
		boolean Wflag = false;
		boolean Nflag = false;
		boolean Sflag = false;

		if (world.isValidLocation(new Location(loc, EAST)))
			Eflag = true;
		if (world.isValidLocation(new Location(loc, WEST)))
			Wflag = true;
		if (world.isValidLocation(new Location(loc, NORTH)))
			Nflag = true;
		if (world.isValidLocation(new Location(loc, SOUTH)))
			Sflag = true;

		if (Eflag && (world.getThing(new Location(loc, EAST)) != null)) {
			if (world.getThing(new Location(loc, EAST)).getClass() == (target.getClass())) {
				return EAST;
			}
		}
		if (Wflag && (world.getThing(new Location(loc, WEST)) != null)) {
			if (world.getThing(new Location(loc, WEST)).getClass() == (target.getClass())) {
				return WEST;
			}
		}
		if (Nflag && (world.getThing(new Location(loc, NORTH)) != null)) {
			if (world.getThing(new Location(loc, NORTH)).getClass() == (target.getClass())) {
				return NORTH;
			}
		}
		if (Sflag && (world.getThing(new Location(loc, SOUTH)) != null)) {
			if (world.getThing(new Location(loc, SOUTH)).getClass() == (target.getClass())) {
				return SOUTH;
			}
		}
		return null;
	}

	/**
	 * finds target inside actor's viewRange, and return that direction.
	 * 
	 * @return returns direction where target is located. if not, returns any
	 *         adjacent vacant direction.
	 */
	public Direction getPreyDir(World world, Actor actor, Object target) {
		Location curLoc = world.getLocation(actor);
		int view = actor.getViewRange();
		int distance = view * 2;
		Direction tempdir = null;

		for (int x = curLoc.getX() - view; x <= curLoc.getX() + view; x++) {
			for (int y = curLoc.getY() - view; y <= curLoc.getY() + view; y++) {
				Location temp = new Location(x, y);
				if (world.isValidLocation(temp)) {
					if (world.getThing(temp) != null) {
						if (world.getThing(temp).getClass() == (target.getClass())) {
							if (curLoc.distanceTo(temp) < distance) {
								distance = curLoc.distanceTo(temp);
								tempdir = curLoc.dirTo(temp);
							}
						}
					}
				}
			}
		}

		if (tempdir == null)
			tempdir = getVacantDir(world, curLoc);

		return tempdir;
	}
	
	/**
	 * Returns a random, vacant Direction using randomDir().
	 *
	 * @param world
	 *            The world to search.
	 * @param loc
	 *        	  location.
	 *
	 * @return A random vacant direction, or null if the adjacent is full.
	 */
	public Direction getVacantDir(World world, Location loc) {
		if (world == null){
			throw new NullPointerException("World cannot be null.");
		} else if (loc == null) {
			throw new NullPointerException("Location cannot be null.");
		}
		
		while (true) {
			Direction dir = Util.randomDir();
			// seed is consistent, so this might result same even if random
			Location dest = new Location(loc, dir);
			if (world.isValidLocation(dest)) {
				if (world.getThing(dest) == null) {
					return dir;
				}
				// if loc is surrounded by objects in every direction
				else if (!abletoMove(world,loc)) {
					System.out.println("Nowhere to move, check other direction");
					break;
				}
			}
		}
		// if every direction is surrounded
		return null;
	}
	
	/**
	 * Checks location's status if location's adjacent directions are valid
	 * and empty.
	 * 
	 * @param world
	 *            The world to search.
	 * @param loc
	 *        	  location.
	 *
	 * @return true if it is able to move.
	 */
	public boolean abletoMove(World world, Location loc) {
		
		final Location locEast = new Location(loc, Direction.EAST);
		final Location locWest = new Location(loc, Direction.WEST);
		final Location locNorth = new Location(loc, Direction.NORTH);
		final Location locSouth = new Location(loc, Direction.SOUTH);
		
		// When East is not valid
		if(!world.isValidLocation(locEast)){
			// East & North is not valid
			if(!world.isValidLocation(locNorth)){
				if(world.getThing(locWest) != null && world.getThing(locSouth) != null)
					return false;
			}
			// East & South is not valid
			else if(!world.isValidLocation(locSouth)){
				if(world.getThing(locWest) != null && world.getThing(locNorth) != null)
					return false;
			}
			// only East is not valid
			else{
				if(world.getThing(locWest) != null && world.getThing(locSouth) != null 
						&& world.getThing(locNorth) != null)
					return false;
			}
		}
		// When West is not valid
		else if(!world.isValidLocation(locWest)){
			// West & North is not valid
			if(!world.isValidLocation(locNorth)){
				if(world.getThing(locEast) != null && world.getThing(locSouth) != null)
					return false;
			}
			// West & South is not valid
			else if(!world.isValidLocation(locSouth)){
				if(world.getThing(locEast) != null && world.getThing(locNorth) != null)
					return false;
			}
			// only West is not valid
			else{
				if(world.getThing(locEast) != null && world.getThing(locSouth) != null 
						&& world.getThing(locNorth) != null)
					return false;
			}
		}
		// When North is not valid
		else if (!world.isValidLocation(locNorth)) {
			if (world.getThing(locEast) != null && world.getThing(locSouth) != null && world.getThing(locWest) != null)
				return false;
		}
		// When South is not valid
		else if (!world.isValidLocation(locSouth)) {
			if (world.getThing(locEast) != null && world.getThing(locNorth) != null && world.getThing(locWest) != null)
				return false;
		}
		// When every direction is valid, but surrounded by objects in every direction
		else if(world.getThing(locEast) != null && world.getThing(locWest) != null &&
				world.getThing(locSouth) != null && world.getThing(locNorth) != null){
			return false;
		}
		
		// after all conditional statements, this loc is able to move
		return true;
	}
}
