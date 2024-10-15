To use tpyst you must install it.
Go to [this](https://github.com/typst/typst) link to find instructions on how as well as more detailed documentation.

To compile the pdf go into the directory of the .typ file and use the one of following commands:
```
# Creates `file.pdf` in working directory.
typst compile file.typ

# Creates PDF file at the desired path.
typst compile path/to/source.typ path/to/output.pdf
```
Once compiled it can be re-compiled or left alone, changes to the .typ file will not be seen in the pdf unless recompiled 

There is also an option to incrementally compile as you go with the command:
```
# Watches source files and recompiles on changes.
typst watch file.typ
```
You may also use a preview tool, I recomend tinymist for VSCode. You still need to compile the PDF to get the actual file however.

All this is available on the typst github linked above :), if you simply want to compile the pdf these commands should be all you need.
