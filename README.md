# WynnTrans

Thanks to [kyaco](https://github.com/kyaco/WynnTextReplacer)

## About

This mod reconstructs [Wynncraft](https://wynncraft.com/) text into a translatable form.
<br>
Each Wynncraft text objects are used to generate a translation map, and recorded in resource pack as language file.

Most features are WIP.

## Implemented Features

- Text rebuilding into translatable text.
- Recording of text that is not registered in a resource pack
- Translating Sign block's text

## How to Use

- Mod jar file and Resource Pack : [Releases](https://github.com/Hexagreen/WynnTrans/releases)

1. You must apply downloaded resource pack before connect to Wynncraft server.
2. Mod will detect translatable texts and replace it into selected Minecraft language if translation exist in resource pack.

If you use [Wynntils/Artemis](https://github.com/Wynntils/Artemis) you should follow these settings.
> Chat - Chat Tabs feature must be disabled.
>
> Chat - Chat Mentions - Highlight Mentions feature must be disabled.

Several Wynntils features, especially control chatting texts, may conflict with this mod.

## About Text Mining and Resource Pack

Exclusive resource pack contains Wynncraft's text (dialogs, informational texts or sign texts...) for register translation keys in Minecraft translation storage.
This mod records unregistered text automatically, but translation resource pack should be made by hands.
Sign block's texts aren't be recorded automatically. If you meet untranslated sign, right-click with soul point on sign.

Logged texts will save in `(Minecraft Directory)\WynnTrans\`. There will be 5 files there.
- `scannedTexts.json` file will store texts that WynnTrans has detected but unregistered in resource pack.
- `getString.txt`, `literal.txt` and `json.txt` will record texts WynnTrans cannot detect.
- `exception.txt` contains error occured texts while mod works.

`scannedTexts.json` has translation key-value pair. You can move these to resource pack lang file and translation into your language.
Resource pack's lang file must be named `LANGCODE.json` likes `ko_kr.json` for Korean or `en_us.json` for English.

Your .txt files in WynnTrans directory contains some new data, it would be helpful if you could report it.

## I Want to Upload my Translations!

Upload it [Issue Page](https://github.com/Hexagreen/WynnTrans/issues) with Translation Upload tag.
It will be approved when I notice that.
But please don't scam with that. I cannot understand all language and your country's players will be angry to scamming.

## Where is Forge Version? / Updates Too Late Bro

Sorry. This mod is my first project, and I don't have much free time. Really sorry for snail-speed updates.

It's hard to care about Forge version.
(I thought about Architectury at first, but I couldn't find enough references. I was soooooo newbie, so I gave it up that time.)
If someone can code/fork into Forge is there, free to fork this mod.

## License

This mod is licenced under the MIT licence, see LICENSE.
