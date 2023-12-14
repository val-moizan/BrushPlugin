# Brush Plugin

Brush plugin is a plugin that allows players to draw by looking at blocks.  
Players can modify their brush using the /brush command.   
It allows them to change the brush range or the brush material by clicking a block in their inventory.

## Compatibility

Currently, the plugin is available for 1.20.2 (i haven't test other versions yet)

## Setup

To setup the plugin, you need a valid mongo db database.  
You can follow the tutorial here to setup one : https://www.mongodb.com/docs/atlas/getting-started/?jmp=docs_driver_java  
Once done, you have to create a database named "brush_plugin" and a collection inside "player_data".  
Then, put the link to access your database from a driver in the "config.properties" file.
That's it, you can use the plugin on your server :)