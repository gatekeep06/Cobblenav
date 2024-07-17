# Update 1.3.5

- Added spanish translations by josuburga.
- Added key bindings for changing buckets on the location screen.
- Added an option to specify the name of the trainer added by the CobblemonTrainers mod, when defeating which will be given a particular badge.
- Fixed a bug that occurred if the badge name in the config had capitalized letters (I'm actually extremely dumb).

# Update 1.3.4

- Added only seen pokemon mode. 

Can be enabled in the config. In this mode, only those pokemon that the player has previously seen will be displayed on the location screen. For this mode to work, either Cobblemon Counter or Rafa's Cobbledex must be installed. If both are installed, Cobblemon Counter will be prioritized.

- Added key bindings to open the main screen and location screen.

The keys are not assigned by default. To use them, you need to have a pokenav in your inventory.

- Slightly improved badges.
  - Received badges now also store information about who granted the badge and the date of granting.
  - Added suggestions for the ```/badge grant``` command.
  - The badge names are now in color💅.

- Fixed giving egg moves to pokemon with special characters in their name. It is now possible.
- Added protection against deleting player data elements, which could cause an error when entering the world.

# Update 1.3.3

- Fixed screen blinking that occurred when trying to render a pokemon with corrupted animations. Now such pokemon will be hidden and the problem will be reported in chat and logs.
- Added an alternative mode for the finder, which can be activated in the client config separately for pokenav and pokefinder. In the alternative mode, the search engine will search for the closest pokemon instead of the best ones.
- Updated the scroll bar slider. Now more compact and correct in terms of design.

# Update 1.3.2

- Fixed exception occurrence if trainer's name is longer than 16 characters.
- Added a setting to the config that allows to configure how trainers will be recorded in player contacts (name and title).
- Added ability to add players who are not on the server to contacts via command, using their names.
- Fixed display of skins in the contact list. Now they are displayed correctly even if players are not on the server (unless, of course, you are a pirate).
- Updated highlighting of contacts in the list. Also by this fixed a rare bug with crawling line, which caused its immobilization.
- Added tooltips with type names to displayed badges.
- Now if your mouse buttons are swapped, in the pokenav they will be swapped as well.
- Added a setting to the client config to enable button tooltips.
- Added a setting to the client config that allows to increase the size of the area, when the cursor is in which the action buttons of the pokemon spawn widget are displayed.
- Fixed missing translation for the netherite pokenav.
- Slightly changed the code structure.

# Update 1.3.1

- Added netherite pokenav (I'll probably redraw this later).
- Added feature to restore the secret pokenav back to its original appearance.
- Fixed crash when opening the contacts screen.
- Removed possibility to click on the "Let's catch" button before the client receives the data from the server.

# Update 1.3.0

- Added Cobblemon Counter Integration.

Now you don't need to install a separate mod to use the integration. It is enough to change the corresponding value in the config to true.

- Added player stats screen.

This screen will display your stats for PvP battles, pokemon captures, pokemon evolutions, the start date of your adventure, and your most frequently used pokemon.

- Revamped contact saving and the contact screen.

Your contacts will no longer be lost after death, as they are now stored in cobblemonplayerdata instead of in the player's nbt. In addition, the contacts screen has received some nice interface changes.

- Added feature to scale pokenav screens by using a value in the client config.
- Added Gholdengo-themed pokenav.
- Added a super secret pokenav model that can be obtained under super secret conditions.
- Added tooltips to pokenav items to indicate color and model.
- Fixed streak display for pokemon with special characters in their name (e.g. Farfetch'd).

# Hotfix 1.2.5

- Fixed an early initialization of the config that caused some other mods' mixins to be unable to be applied.

# Update 1.2.4

- Added CobblemonTrainers Integration. 

If you have CobblemonTrainers installed and using the integration is allowed in the config, after a battle with a trainer, it will be logged in your contacts, just like the players.

- Changed the moment when a pokemon is saved for hunting.

Now it happens when you go to the finder screen, not when you press the “Let's catch” button. The change is intended to make pokefinder easier to use

- The tooltips now also display more detailed spawn chances.
- Added more client config parameters.

_A guide to all the parameters will definitely be there... someday_

- Added more parameters by which the finder chooses the best pokemon.
- The finder now also displays if the pokémon is shiny.
- Added possibility to customize weights for all parameters for choosing the best pokemon in the config.
- Added command to check server config.

```
/cobblenav checkConfig
```

- Fixed minor UI issues.

# Update 1.2.3

![team-widget.png](https://cdn.modrinth.com/data/bI8Nt3uA/images/91b5a3e246c1c81faab8dd1833cf66b33cf1412d.png)

#### _Compatible with Cobblemon 1.5.0_

- Added Pokefinder item. Pokefinder allows you to avoid having to open a pokenav multiple times to track the same pokemon. All you have to do now is use a pokefinder to find a pokemon nearby and start tracking it right away.
- Revamped team widget. All pokemon are now displayed in their actual scale.
- Added preferences saving. Bucket and sorting type are now saved when closing a pokenav and will be restored when the pokenav is reopened.
- Revamped config.
- Updated pokenav textures.
- Updated last found pokemon button.
- Added recipes for restoring standard pokenav from painted ones, as well as a mirrored pokenav recipe.
- Fixed clearContacts command.