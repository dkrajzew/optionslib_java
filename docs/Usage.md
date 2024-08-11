# Interface
The major interface to the options is the class "OptionsCont".

I usually have a method (function) that builds an OptionsCont, fills it with options and calls the parsing method to set the values given on the command line. What it does first is to build the options interface.

```java
 OptionsCont options = new OptionsCont();
```

## Adding Options
An option is added to the OptionsCont using the "add" method:
```java
 void add(char abbr, Option option);
 void add(String name, Option option);
 void add(String name, char abbr, Option option);
```

Please notice that neither the full name nor the abbreviation contain the leading "--" or "-", respectively. One usually does not use an abbreviation only.

The option is a new built instance of one of:

* Option_Bool
* Option_Integer
* Option_Double
* Option_String

Each of these type-aware option classes can get a value which will be used as default.

If you like to have more names for an option, e.g. --input-file, --input, and -f, you may additionally use the OptionsCont method "addSynonym":
```java
 options.add("input-file", 'f', new Option_String());
 options.addSynonym("input-file", "input");
```

## Parsing Options and Reading Configurations
The major interface for parsing options given on the command line and/or reading a configuration file is the method ```void OptionsIO::parseAndLoad(options, args, configOptionName, boolean continueOnError, boolean acceptUnknown)```. Here ```configOptionName``` is the name of the option to read the name of the configuration file from. If no name is supported, no configuration is read. This is as well the case if the named option is not set.

## Retrieving Options
You directly ask for an option's value in a type-aware name:
```java
 int intValue = options.getInteger("my-int-option");
 double doubleValue = options.getDouble("my-double-option");
 boolean boolValue = options.getBool("my-bool-option");
 String strValue = options.getString("my-string-option");
```

You can additionally ask whether an option exists (albeit you should know this) using ```boolean contains(String name) const``` and whether an option is set (a value has been given) using ```boolean isSet(String name) const```.

When asking for an option, I recommend to use the full name, so to ask for "help" and not for "?". "?" may be obvious, but simple characters have two disadvantages: a) you may get lost when having too many of them; b) when incrementally adding new options during development, it is often necessary to decide which one to abbreviate and which not. So the abbreviations change more often than the full names, requiring to adapt the code more often.

## Help and Sectioning
Usually, the help screen contains a short description about each option's purpose. Add such a description to a previously added option using the method ```void setDescription(String name, String desc)```. 

If you have many options, it may make sense to group them thematically. Use the ```void beginSection(String name)``` method to start a new section.

In addition, you may add a head and a tail to your help screen that preceed, follow the list of options, respectively. This is done using ```void setHelpHeadAndTail(String head, String tail)```.

To print the help screen use the static method ```void printHelp(PrintStream os, int maxWidth, int optionIndent, int divider, int sectionIndent)``` from the OptionsIO class.

A basic behaviour for an 80-characters wide terminal could be: ```OptionIO.printHelp(System.out, 80, 2, 2, 1);```

## Further information
* [doxygen class documentation](https://www.krajzewicz.de/docs/optionslib_java/index.html)
