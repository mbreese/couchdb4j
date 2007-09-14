svn rm javadoc
svn ci -m "Removed old Javadocs."
ant javadoc
svn add javadoc
svn ci -m "Added updated Javadocs."