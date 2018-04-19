chdir('lab2');
system("del /S *.class");
system('javac Main.java -encoding utf-8');
my @files = ("BinaryTree", "BubbleSort", "Factorial", "LinearSearch", "LinkedList", "MoreThan4", "QuickSort", "TreeVisitor");
for my $i (@files) {
    print "$i\n";
    my $out_pg = `java Main ../ztest/$i.java > ../run/tmp.pg`;
    my $out = `java -jar ../run/pgi.jar < ../run/tmp.pg`;
    my $ans = `java -jar ../run/pgi.jar < ../run/$i.pg`;
    if ($out ne $ans) {
        exit(10086);
    }
}
