package faceduck.actors;

import faceduck.ai.RabbitAI;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Rabbit;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

public class RabbitImpl implements Rabbit {
	private static final int RABBIT_MAX_ENERGY = 15;
	private static final int RABBIT_VIEW_RANGE = 3;
	private static final int RABBIT_BREED_LIMIT = RABBIT_MAX_ENERGY * 3 / 4;
	private static final int RABBIT_ENERGY_VALUE = 10;
	private static final int RABBIT_COOL_DOWN = 4;
	private static final int RABBIT_INITAL_ENERGY = RABBIT_MAX_ENERGY * 1 / 2;

	private int RABBIT_CURRENT_ENERGY = RABBIT_INITAL_ENERGY;
	private int RABBIT_CURRENT_COOLDOWN = 0;
	private RabbitAI rabbitAI = new RabbitAI();
	private Location loc;	

	@Override
	public void act(World world) {
		// TODO Auto-generated method stub
		if (world == null) {
			throw new NullPointerException("World must not be null.");
		}

		if (RABBIT_CURRENT_COOLDOWN == RABBIT_COOL_DOWN) { // cool time done
			RABBIT_CURRENT_COOLDOWN = 0;
			RABBIT_CURRENT_ENERGY--;
			
			Command action = rabbitAI.act(world, this);	
			if (action == null) { // if actor is unable to take any action
				System.out.println("unable to act!!");
			} else { // take action
				action.execute(world, this);
			}
		}
		else { // cool time not yet
			RABBIT_CURRENT_COOLDOWN++;
		}
		
		if(RABBIT_CURRENT_ENERGY == 0) {
			world.remove(this);
		}
	}

	@Override
	public void eat(World world, Direction dir) {
		// TODO Auto-generated method stub
		loc = world.getLocation(this);
		Location dest = new Location(loc, dir);
		
		// eat grass
		if(world.getThing(dest) instanceof Grass){
			Grass adjGrass = (Grass) world.getThing(dest);
			this.RABBIT_CURRENT_ENERGY += adjGrass.getEnergyValue();
			if(this.RABBIT_CURRENT_ENERGY > this.getMaxEnergy()){
				this.RABBIT_CURRENT_ENERGY = this.getMaxEnergy();
			}
			world.remove(world.getThing(dest));
		}
		else{
			System.out.println("this destination is not Grass!!");
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

		RabbitImpl child = new RabbitImpl();
		this.RABBIT_CURRENT_ENERGY = this.RABBIT_CURRENT_ENERGY * 1 / 2;
		child.RABBIT_CURRENT_ENERGY = this.RABBIT_CURRENT_ENERGY;
		world.add(child, dest);
	}

	@Override
	public int getEnergy() {
		// TODO Auto-generated method stub
		return RABBIT_CURRENT_ENERGY;
	}

	@Override
	public int getMaxEnergy() {
		// TODO Auto-generated method stub
		return RABBIT_MAX_ENERGY;
	}

	@Override
	public int getBreedLimit() {
		// TODO Auto-generated method stub
		return RABBIT_BREED_LIMIT;
	}

	@Override
	public int getViewRange() {
		// TODO Auto-generated method stub
		return RABBIT_VIEW_RANGE;
	}

	@Override
	public int getCoolDown() {
		// TODO Auto-generated method stub
		return RABBIT_COOL_DOWN;
	}

	@Override
	public int getEnergyValue() {
		// TODO Auto-generated method stub
		return RABBIT_ENERGY_VALUE;
	}
	
}

