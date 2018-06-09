my $exit_code = 0;

chdir('lab2');
system("perl test2.pl");
$exit_code = 1 if ($? != 0);

chdir('../lab3');
system("perl test3.pl");
$exit_code = 1 if ($? != 0);

chdir('../lab4');
system("perl test4.pl");
$exit_code = 1 if ($? != 0);

chdir('../lab5');
system("perl test5.pl");
$exit_code = 1 if ($? != 0);

exit($exit_code);
