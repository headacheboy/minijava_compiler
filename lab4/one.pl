system("javac S2K.java");
my $file = "..\\run\\Factorial.spg";
$file = $ARGV[0] if $ARGV[0];
system("java S2K $file");
