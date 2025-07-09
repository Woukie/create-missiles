# Create Missiles

![icon_256](<https://raw.githubusercontent.com/Woukie/create-missiles/refs/heads/1.20.1/icons/icon_256.gif>)

Minecraft missle mod for Forge and Fabric, depends on Create.

## Features

List is subject to change.

- Missile build animations
  - Each assembly (warhead, chassis and thruster) has a unique model that changes with build percentage
- 'Assemblies'
  - Assemblies are blueprints for missile parts used by the `Assembly Panel` and can be mixed and matched, i.e. `Nuclear Warhead + Copper Chassis + Ender Thruster`
  - Each assembly has a unique way of being obtained and can be cloned once in your possession
- Launch pad
  - Placed in a 3x3 horizontal plane
  - Accepts recipe ingredients from mechanical arms
  - Requires power
  - Place the three panels anywhere touching the launch pad
- Navigation panel
  - Takes a filled map
  - Click anywhere on the shown map to set the target
  - Flight is simulated, the target angle estimated, and resultant trajectory is plotted on the right
  - Slider sets the boost phase time, larger boost phases give you higher altitudes
- Control panel
  - Shows a console and a panel for the launch button
  - Displays system status
  - Displays recipie status
- Assembly panel
  - Place your warhead, chassis and thruster assemblies into this
  - A recipe will appear in the control panel
  - The launch pads will now accept recipe ingredients from mechanical arms and hoppers
- Drone slingshot
  - Place a drone in the slot and fire
  - Write
  - This activates the drone
- Drones
  - Drones take surveys of areas
- Potential mod integration
  - CBC to shoot down missiles
  - Show on Create Radar's radars 
  - Config to disable mod integration for a specific mod
  - New warheads for nuclear create mods
  - Any TNT/explosives mod

## Implementation and Distribution

Implementation details, subject to change

- Built on Architectury, for Fabric and Forge
- Development starts with 1.20.1
- Distributed on GitHub, CurseForge and Modrinth
- Licenced under `GNU GENERAL PUBLIC LICENSE V3`
- Uses the hybrid versioning style defined [here](https://forge.gemwire.uk/wiki/Semantic_Versioning)
- A new branch is created for each version of Minecraft
- If you see another site distributing the mod, it's not me. Lmk if you want me to consider distributing elsewhere
