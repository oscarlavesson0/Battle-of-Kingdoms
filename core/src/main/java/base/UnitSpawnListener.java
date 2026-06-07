package base;

import unit.CustomUnit;

/**
 * Listener notified when a unit is spawned.
 *
 * @author Oscar Lavesson
 */
public interface UnitSpawnListener {
    /**
     * Called when a unit has been spawned.
     *
     * @param unit the spawned unit
     */
    public void onUnitSpawned(CustomUnit unit);
}
