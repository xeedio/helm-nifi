// usage: groovy nifi-test-el.groovy '${now():toNumber():minus(3999900000):format("yyyy-MM-dd HH:mm:ss", "GMT")}'
// source: https://community.cloudera.com/t5/Community-Articles/Testing-NiFi-Expression-Language-with-Groovy/ta-p/247208
@Grab(group='org.apache.nifi', module='nifi-expression-language', version='1.4.0')
import org.apache.nifi.attribute.expression.language.*

def cli = new CliBuilder(usage:'groovy testEL.groovy [options] [expressions]',
                          header:'Options:')
cli.help('print this message')
cli.D(args:2, valueSeparator:'=', argName:'attribute=value',
       'set value for given attribute')
def options = cli.parse(args)
if(!options.arguments()) {
  cli.usage()
  return 1
}

def attrMap = [:]
def currKey = null
options.Ds?.eachWithIndex {o,i ->
  if(i%2==0) {
    currKey = o
  } else {
    attrMap[currKey] = o
  }
}
options.arguments()?.each {
  def q = Query.compile(it)
  println q.evaluate(attrMap ?: null)
}
