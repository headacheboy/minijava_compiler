system("javac K2M.java");
my $file = "../run/Factorial.kg";
$file = $ARGV[0] if $ARGV[0];
system("java K2M $file");
