package faceduck.commands;

import faceduck.actors.Almighty;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Location;

/**
 * This command calls an {@link Almighty} to transform Rabbit to Grass.
 */
public class RabbittoGrassCommand implements Command {
	private Location curLoc;
	/**
	 * Instantiates RabbittoGrass Command.
	 */
	public RabbittoGrassCommand (Location loc) {
		this.curLoc = loc;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException
	 *             If actor is not an instance of Animal.
	 */
	@Override
	public void execute(World world, Actor actor) {
		if (actor == null) {
			throw new NullPointerException("Actor cannot be null");
		} else if (world == null) {
			throw new NullPointerException("World cannot be null");
		} else if (!(actor instanceof Animal)) {
			throw new IllegalArgumentException(
					"actor must be an instance of Animal.");
		}
		((Almighty) actor).Rabbit_to_Grass(world, curLoc);
	}
}
