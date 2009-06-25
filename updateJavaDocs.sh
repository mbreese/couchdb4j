rm -rf javadoc
svn update javadoc
svn rm javadoc
svn ci -m "Removed old Javadocs."
ant javadoc
svn add javadoc

find javadoc -name *html -exec svn propset svn:mime-type text/html \{\} \;
find javadoc -name *gif -exec svn propset svn:mime-type image/gif \{\} \;

svn ci -m "Added updated Javadocs."
