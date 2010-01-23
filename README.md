Markdown Plus
==============

Markdown Plus is a Java/Groovy port of [Markdown][].
Markdown Plus will process your md documents and give you a pretty html or pdf.
Not that you need for most things since markdown is meant to be just as readable as a text document

  [Markdown]:http://daringfireball.net/projects/markdown/syntax
  
Introduction
------------

Markdown Plus is a Java/Groovy port of the [Markdown][] program written by John Gruber and the syntax of course 100% supports core [Markdown][].

"The Markdown syntax allows you to write text naturally and format it without using HTML tags. More importantly: in Markdown format, your text stays enjoyable to read for a human being, and this is true enough that it makes a Markdown document publishable as-is, as plain text. If you are using text-formatted email, you already know some part of the syntax." - [PHP Markdown](http://michelf.com/projects/php-markdown/)

Markdown Plus is 3 things: 

 1. a plain text syntax for any type of documentation or README, and 
 2. a tool that converts the plain text markup to HTML for publishing on the web or printing to PDF
 3. extended syntax to the [Markdown][] heavily based on the the Perl derivitive called [MultiMarkdown][] and with much inspiration from [PHP Markdown Extra][]

 [MultiMarkdown]: http://fletcherpenney.net/multimarkdown/
 [PHP Markdown Extra]: http://michelf.com/projects/php-markdown/extra/

Installation and Running
------------

Markdown Plus requires java 1.5 or later.

Add to your PATH  the directory where you unzipped or checked-out Markdown Plus. This would be the root directory where the mdp script or bat is located. 

Syntax
------

See the excellent [MultiMarkdown Syntax][] for what Markdown Plus supports.

What we "mostly" support from MultiMarkdown

*   [Tables](http://fletcherpenney.net/multimarkdown/users_guide/multimarkdown_syntax_guide/#anchorandimageattributes)
    this is supported but to the extent that [PHP Markdown Extra][] supports it and not with row spans 
      and the more complicated setup that MultiMarkdown supports

*   [Image Support](http://fletcherpenney.net/multimarkdown/users_guide/multimarkdown_syntax_guide/#imagesupport)

*   [Anchor and Image Attributes](http://fletcherpenney.net/multimarkdown/users_guide/multimarkdown_syntax_guide/#anchorandimageattributes)

*   Heading [Automatic Cross-References](http://fletcherpenney.net/multimarkdown/users_guide/multimarkdown_syntax_guide/#automaticcross-references)

What we don't support from MultiMarkdown

* bibliography stuff
* the xmlt translations he does. although you can always download his. Most any syntax that works with Markdown Plus will work with his.

  [MultiMarkdown Syntax]:http://fletcherpenney.net/multimarkdown/users_guide/multimarkdown_syntax_guide/

TODO
-----
-table caption
-CSS





