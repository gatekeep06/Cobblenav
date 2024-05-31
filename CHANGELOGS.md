# Update 1.3.0

- Added Cobblemon Counter Integration.
- Added player stats screen.
- Revamped contact saving and the contact screen.
- Added feature to scale pokenav screens by using a value in the client config.
- Added Gholdengo-themed pokenav.
- Added a super secret pokenav model that can be obtained under super secret conditions.
- Added tooltips to pokenav items to indicate color and model.

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