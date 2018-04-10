当局部变量未初始化，直接输出MAIN ERROR END
其他运行时错误则会在内部生成一些ERROR语句（比如类没有new，数组没有new，数组越界）

编译：
javac Main.java -encoding utf-8 
运行：
java Main < pathInput > pathOutput
测试：（有pgi.jar）
java -jar pgi.jar < path
