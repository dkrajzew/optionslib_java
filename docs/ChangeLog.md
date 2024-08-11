## master
### New features
* [issue #2](https://github.com/dkrajzew/optionslib_java/issues/2) added CSV-configurations support
* Moved the documentation from the Wiki to an own markdown folder

### Debugging / Refactoring
* extracted XML reading and writing methods from OptionsIO to OptionsTypedFileIO.h (interface) and its implementations

## version 1.2 (06.08.2021)

### New features
* writing templates
* writing configurations

### Debugging / Refactoring
* correcting misspelled "synonyms"
* improved error reporting
* API extensions
  * OptionsCont: added ```public String getValueAsString(String name)``` for retrieving the string representation of a named option's value
  * OptionsCont: added ```public String getTypeName(String name)``` for retrieving a named option's data type name
  * OptionsCont: added ```public bool isDefault(String name)``` for retrieving whether the value of an option is its default value
  * OptionsCont: added ```public String getSection(String optionName)``` for retrieving the help section of an option
  * OptionsCont: added ```public String getDescription(String optionName)``` for retrieving an option's help description
  * OptionsCont: added ```public String getHelpHead()``` for retrieving the head of the help output
  * OptionsCont: added ```public String getHelpTail()``` for retrieving the tail of the help output

## version 1.0 (08.10.2019)
* base
