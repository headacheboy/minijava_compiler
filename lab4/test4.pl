system("del /S *.class");
# system('javac Main.java -encoding utf-8');
my @files = ("BinaryTree", "BubbleSort", "Factorial", "LinearSearch", "LinkedList", "MoreThan4", "QuickSort", "TreeVisitor");
my $exit_code = 0;
for my $i (@files) {
    print "$i\n";
    # my $out_kg = `java Main ../run/$i.java > ../run/tmp.kg`;
    # my $out = `java -jar ../run/pgi.jar < ../run/tmp.kg`;
    my $out = "asd\n";
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
