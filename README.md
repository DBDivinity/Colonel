# Colonel

A 1.7.10 Compatible Minecraft Mod that enables the usage of Mojang's Brigadier system.

## Usage
### For Server Owners:
Download the latest `colonel-$version-server.jar` from the releases page and put it in the server's mod folder.
### For Developers
Download the latest `colonel-$version.jar` file from the releases page and put in your mod's `./libs` folder.

Afterwards, add
```
maven{
    name = "mojang"
    url "libraries.minecraft.net"
}
```
to the list of repositories in your `build.gradle`.

Finally, add `"com.mojang:brigadier:1.0.18"` as a dependency.
## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update versioning as appropriate.
## License

[MIT](https://choosealicense.com/licenses/mit/)
