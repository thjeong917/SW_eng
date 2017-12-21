package faceduck.actors;

import faceduck.ai.GnatAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

/**
 * This is a simple implementation of a Gnat. It never loses energy and moves in
 * random directions.
 */
public class Gnat implements Animal {
	private static final int MAX_ENERGY = 10;
	private static final int VIEW_RANGE = 1;
	private static final int BREED_LIMIT = 0;
	private static final int COOL_DOWN = 0;
	private int INI_ENERGY;
	private GnatAI gnatAI = new GnatAI();
	
	public Gnat (int degree){
		this.INI_ENERGY = degree;
	}
	
	@Override
	public void act(World world) {
		// TODO Auto-generated method stub
		if (world == null) {
			throw new NullPointerException("World must not be null.");
		}
		Command action = gnatAI.act(world, this);
		
		if(action == null)
			return;
		else
			action.execute(world, this);
	}
	@Override
	public int getViewRange() {
		// TODO Auto-generated method stub
		return VIEW_RANGE;
	}
	@Override
	public int getCoolDown() {
		// TODO Auto-generated method stub
		return COOL_DOWN;
	}
	@Override
	public int getEnergy() {
		// TODO Auto-generated method stub
		return INI_ENERGY;
	}
	@Override
	public int getMaxEnergy() {
		// TODO Auto-generated method stub
		return MAX_ENERGY;
	}
	@Override
	public int getBreedLimit() {
		// TODO Auto-generated method stub
		return BREED_LIMIT;
	}
	@Override
	public void move(World world, Direction dir) {
		// TODO Auto-generated method stub
		Location loc = world.getLocation(this);
		// moves to random direction
		Location dest = new Location(loc, dir);
		Object temp = this;
		world.remove(this);
		world.add(temp, dest);
	}
	
	@Override
	public void eat(World world, Direction dir) {
		// TODO Auto-generated method stub
	}

	@Override
	public void breed(World world, Direction dir) {
		// TODO Auto-generated method stub		
	}
}
