desc "getopt4j: Parse command line arguments in java."
define 'getopt4j' do
  project.version = '1.0-SNAPSHOT'
  project.group = 'org.realityforge'

  compile.options.source = '1.6'
  compile.options.target = '1.6'
  compile.options.lint = 'all'

  iml.main_source_directories << _("examples")

  package(:jar)
end
