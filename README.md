# WynnTrans

Thanks to [kyaco](https://github.com/kyaco)

**WynnTrans** is a third-party i18n project for the **Wynncraft, The Minecraft MMORPG**. This mod modifies in-game text during the text packet receiving or rendering phase on the client-side, making it possible to translate text and support multiple languages using Minecraft's translation system and resource packs.

## Working Structure

This client-side mod modifies in-game text that appears on the **Wynncraft server** by converting it into a translatable form during the **text packet receiving** or **rendering** stages. It supports multiple languages by using Minecraft's **translation system** and **resource packs**.

## Implemented Features

- Chat message translation
- Text Display translation
- Title text translation
- Item tooltip translation
- Sign block text translation
- Logging of untranslated text key-value pairs (untranslated text is saved in the `scannedTexts.json` file)

(Note: The translation base is prepared for each section, but not all text can be translated.)

## Installation

1. Set up your Minecraft and Fabric environment by following the instructions on the [Release page](https://github.com/Hexagreen/WynnTrans/releases).
2. Download and apply the mod and resource pack.

## Usage

1. Activate the resource pack before connecting to the server with the mod enabled.
2. Make sure the mod is active and the resource pack is enabled before entering the server.

Some features of [**Wynntils**](https://github.com/Wynntils/Wynntils) may not work as expected. For detailed information, refer to the recommended settings and limitations listed on the Release page.

## Translation Contribution

To contribute to translations, refer to the README in the [WynnTrans Language Pack](https://github.com/Hexagreen/WynnTrans-Language-Pack) repository.

## About Text Recording and Resource Pack

- The resource pack contains lang files for translating various texts in Wynncraft.
- New texts that are not found in the lang files will be saved in the `scannedTexts.json` file, located in the `(Minecraft Directory)\WynnTrans\` directory.
- Texts that encountered errors during translation will be saved in the `json.txt` file.
- The key-value pairs in the `scannedTexts.json` file follow the same format as those in the resource pack's lang files. If you're not interested in contributing translations, you can safely delete these files periodically.

## Forge Version?

Since this is my first modding project, managing both environments has been challenging. While I might consider a Forge version in the future, it is unlikely to be available in the short term.

## License

This project is licensed under the **MIT License**.
