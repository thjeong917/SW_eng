package faceduck.actors;

import faceduck.ai.FoxAI;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Fox;
import faceduck.skeleton.interfaces.Rabbit;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

public class FoxImpl implements Fox {
	private static final int FOX_MAX_ENERGY = 160;
	private static final int FOX_VIEW_RANGE = 5;
	private static final int FOX_BREED_LIMIT = FOX_MAX_ENERGY * 3 / 4;
	private static final int FOX_COOL_DOWN = 2;
	private static final int FOX_INITAL_ENERGY = FOX_MAX_ENERGY * 1 / 2;
	
	private int FOX_CURRENT_ENERGY = FOX_INITAL_ENERGY;
	private int FOX_CURRENT_COOLDOWN = 0;
	private FoxAI foxAI = new FoxAI();
	private Location loc;
	
	@Override
	public void act(World world) {
		// TODO Auto-generated method stub
		if (world == null) {
			throw new NullPointerException("World must not be null.");
		}
		
		if (FOX_CURRENT_COOLDOWN == FOX_COOL_DOWN) { // cool time done
			FOX_CURRENT_COOLDOWN = 0;
			FOX_CURRENT_ENERGY--;
			
			Command action = foxAI.act(world, this);	
			if (action == null) { // if actor is unable to take any action
				System.out.println("unable to act!!");
			} else { // take action
				action.execute(world, this);
			}
		}
		else { // cool time not yet
			FOX_CURRENT_COOLDOWN++;
		}
		
		if(FOX_CURRENT_ENERGY == 0) {
			world.remove(this);
		}
	}
	
	@Override
	public void eat(World world, Direction dir) {
		// TODO Auto-generated method stub
		loc = world.getLocation(this);
		Location dest = new Location(loc, dir);

		// eat Rabbit
		if (world.getThing(dest) instanceof Rabbit) {
			Rabbit adjRabbit = (Rabbit) world.getThing(dest);
			this.FOX_CURRENT_ENERGY += adjRabbit.getEnergyValue();
			if (this.FOX_CURRENT_ENERGY > this.getMaxEnergy()) {
				this.FOX_CURRENT_ENERGY = this.getMaxEnergy();
			}
			world.remove(world.getThing(dest));
		} 
		else {
			System.out.println("this destination is not Rabbit!!");
		}
	}
	
	@Override
	public void move(World world, Direction dir) {
		// TODO Auto-generated method stub
		loc = world.getLocation(this);
		// moves to random direction
		Location dest = new Location(loc, dir);
		Object temp = this;
		world.remove(this);
		world.add(temp, dest);
	}
	
	@Override
	public void breed(World world, Direction dir) {
		// TODO Auto-generated method stub
		loc = world.getLocation(this);
		Location dest = new Location(loc, dir);
		// make duplicate (Child)

		FoxImpl child = new FoxImpl();
		this.FOX_CURRENT_ENERGY = this.FOX_CURRENT_ENERGY * 1 / 2;
		child.FOX_CURRENT_ENERGY = this.FOX_CURRENT_ENERGY;
		world.add(child, dest);
	}
	
	@Override
	public int getEnergy() {
		// TODO Auto-generated method stub
		return FOX_CURRENT_ENERGY;
	}
	@Override
	public int getMaxEnergy() {
		// TODO Auto-generated method stub
		return FOX_MAX_ENERGY;
	}
	@Override
	public int getBreedLimit() {
		// TODO Auto-generated method stub
		return FOX_BREED_LIMIT;
	}
	@Override
	public int getViewRange() {
		// TODO Auto-generated method stub
		return FOX_VIEW_RANGE;
	}
	@Override
	public int getCoolDown() {
		// TODO Auto-generated method stub
		return FOX_COOL_DOWN;
	}

}
