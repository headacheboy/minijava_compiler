system("del /S *.class");
system('javac K2M.java -encoding utf-8');
my @files = ("BinaryTree", "BubbleSort", "Factorial", "LinearSearch", "LinkedList", "MoreThan4", "QuickSort", "TreeVisitor");
my $exit_code = 0;
for my $i (@files) {
    print "$i\n";
    my $out = `java K2M ../run/$i.kg > ../run/tmp_$i.s`;
    $exit_code = 1 if $? != 0;
    open(my $ans, "< ../run/$i.s");
    open(my $out, "< ../run/tmp_$i.s");
    while (my $l_ans = <$ans>) {
        my $l_out = <$out>;
        chop($l_ans);
        chop($l_out);
        my @ans_nums = $l_ans =~ /(\d+)/g;
        my @out_nums = $l_out =~ /(\d+)/g;
        if (equal_list(\@ans_nums, \@out_nums) == 0) {
            print "$l_ans\n";
            print "$l_out\n";
            $exit_code = 1;
            break;
        }
    }
    close $ans;
    close $out;
}
sub equal_list {
    my ($a, $b) = @_;
    for (my $i=0; $i<@$a; ++$i) {
        if (@$a[$i] != @$b[$i]) {
            return 0;
        }
    }
    return 1;
}
exit($exit_code);
