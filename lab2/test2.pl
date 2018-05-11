system("del /S *.class");
system('javac Main.java -encoding utf-8');
my @files = ("BinaryTree", "BubbleSort", "Factorial", "LinearSearch", "LinkedList", "MoreThan4", "QuickSort", "TreeVisitor");
my $exit_code = 0;
for my $i (@files) {
    print "$i\n";
    my $out_pg = `java Main ../run/$i.java > ../run/tmp.pg`;
    my $out = `java -jar ../run/pgi.jar < ../run/tmp.pg`;
    my $ans = `java -jar ../run/pgi.jar < ../run/$i.pg`;
    if ($out ne $ans) {
        print("**************** error\n");
        print($out);
        print("ans ->\n");
        print($ans);
        $exit_code = 1;
    }
}

my @extra_tests = ("my-override");
for my $i (@extra_tests) {
    print "$i\n";
    my $out_pg = `java Main ../run/$i.java > ../run/tmp.pg`;
    my $out = `java -jar ../run/pgi.jar < ../run/tmp.pg`;
    my $ans = "1\n1\n2\n2\n1\n0\n0\n";
    if ($out ne $ans) {
        print("**************** error\n");
        print($out);
        print("ans ->\n");
        print($ans);
        $exit_code = 1;
    }
}
exit($exit_code);
