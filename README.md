# Seed Scramble

A simple utility to scramble/unscramble cryptocurrency wallet seed phrases

> Brought to you by the **SHOP** stake pool

## WARNINGS

This is not for everyone. Use it if you have trouble trusting your own shadow and
you know what you are doing.
  
**DO NOT use this in your regular computer**. Choose one of the following security measures
 to prevent having your secrets stolen by any malware/spyware you might have running
 on your system:
 
 1. Boot a Linux live CD, download this app, disconnect from the internet, then run it.

 2. Run this in an air-gapped machine that will never connect to the internet

 3. If you intend to connect your computer to the internet after running this software
    format your hard drive and reinstall the operating system from scratch.
    
The key takeway here is to NEVER use this app while connected to the internet
and ensure your system never connects to the internet after you used this app.

**ENSURE YOU TEST EVERYTHING**. After generating the fake seed phrase and 
writing it in a piece of paper, try to recover the original seed.

**THIS IS NOT ENCRYPTION**. The solution presented here simply makes it extremely
hard for another party to recover your original seed phrase if they find your 
fake seed. **Keep this fake seed safe and secure as if it were your
actual seed phrase**.
    
## Purpose

I created this tool for myself to give me peace of mind writing down seed 
phrases for my crypto currency wallets - and that includes hardware wallets.
 
Whenever you setup a hardware wallet or use a software-based wallet to store
cryptocurrency, you will be presented with a list of words that give access to 
your funds in the blockchain.

Writing down this list of words means that if anyone finds these words, they'll
be able to restore your wallet and steal your funds.  

Give this application your seed phrase with a password and a magic number. It
will create a "fake" seed phrase which you can note down. 

If you want to restore your original seed phrase, give this application the
"fake" seed phrase, the password and the magic number.

It's dead simple to use, and no one will ever be able to figure out the actual
seed phrase of your wallets.


### Example

The app is a single screen that looks like this:

![thumbnail](./screenshot.png)

In the example above, we assume your wallet gave the following seed phrase:
```
obvious regular try drama sick play travel bread congress myth broken wrist
```

You can input a password & a magic number to generate a fake seed phrase:

```
opinion universe favorite category soap plastic salon cycle polar wolf bronze scorpion
```

You can note that down in a piece of paper instead of the original seed phrase, then
recover the original by unscrambling the fake seed with the password and magic
number you used. 

If anyone finds the fake seed phrase they won't be able to steal your funds.

## INSTALLATION

Simply download the [latest release](https://github.com/uniVocity/SeedScramble/releases/download/1.0.0/SeedScramble.zip)
from [the releases page](https://github.com/uniVocity/SeedScramble/releases/tag/1.0.0)
and move it to some device with NO INTERNET CONNECTION. You want to do this offline.

Unzip the `SeedScramble.zip` file and double-click the `run` command to launch it:

 * `run.bat` on Windows
 * `run.command` on MacOS
 * `run.sh` on Linux
  
Make sure your system will never connect to the internet while and after you 
use this software. 

