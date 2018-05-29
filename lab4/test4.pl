system("del /S *.class");
system('javac S2K.java -encoding utf-8');
my @files = ("BinaryTree", "BubbleSort", "Factorial", "LinearSearch", "LinkedList", "MoreThan4", "QuickSort", "TreeVisitor");
my $exit_code = 0;
for my $i (@files) {
    print "$i\n";
    my $out_kg = `java S2K ../run/$i.spg > ../run/tmp.kg`;
    my $out = `java -jar ../run/kgi.jar < ../run/tmp.kg`;
    my $ans = `java -jar ../run/kgi.jar < ../run/$i.kg`;
    if ($out ne $ans) {
        print("**************** error\n");
        print($out);
        print("ans ->\n");
        print($ans);
        $exit_code = 1;
    }
}
exit($exit_code);
