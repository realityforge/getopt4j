require 'buildr/git_auto_version'
require 'buildr/gpg'

desc 'getopt4j: Parse command line arguments in java.'
define 'getopt4j' do
  project.group = 'org.realityforge.getopt4j'

  compile.options.source = '1.6'
  compile.options.target = '1.6'
  compile.options.lint = 'all'

  iml.main_source_directories << _('examples')

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/getopt4j')
  pom.add_developer('realityforge', 'Peter Donald')

  package(:jar)
  package(:sources)
  package(:javadoc)
end
