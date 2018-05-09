system("del /S *.class");
system('javac P2S.java');
my @files = ("BinaryTree", "BubbleSort", "Factorial", "LinearSearch", "LinkedList", "MoreThan4", "QuickSort", "TreeVisitor");
my $exit_code = 0;
for my $i (@files) {
    print "$i\n";
    my $out_spg = `java P2S ../run/$i.pg > ../run/tmp.spg`;
    my $out_ok = `java -jar ../run/spp.jar < ../run/tmp.spg`;
    if ($out_ok ne "Program parsed successfully\n") {
        print("**************** error\n");
        print($out_spg);
        print($out_ok);
        $exit_code = 1;
        next;
    }
    my $out = `java -jar ../run/pgi.jar < ../run/tmp.spg`;
    my $ans = `java -jar ../run/pgi.jar < ../run/$i.pg`;
    if ($out ne $ans) {
        print("**************** error\n");
        print($out);
        print("ans ->\n");
        print($ans);
        $exit_code = 1;
    }
}
exit($exit_code);
