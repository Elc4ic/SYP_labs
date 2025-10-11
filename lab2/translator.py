import re

class KotlinToPythonTranslator:
    rules = [
        (r"fun (\w+)\((.*?)\):?\s*(\w+)?\s*{", r"def \1(\2):"),
        (r"fun (\w+)\((.*?)\)\s*{", r"def \1(\2):"),
        (r"val (\w+):?\s*\w* = (.+)", r"\1 = \2"),
        (r"var (\w+):?\s*\w* = (.+)", r"\1 = \2"),
        (r"if \((.+)\)\s*{", r"if \1:"),
        (r"}\s*else if \((.+)\)\s*{", r"elif \1:"),
        (r"}\s*else\s*{", r"else:"),
        (r"for \((\w+) in (\d+)\.\.(\d+)\)\s*{", r"for \1 in range(\2, \3+1):"),
        (r"for \((\w+) in (\d+)\.\.<(\d+)\)\s*{", r"for \1 in range(\2, \3):"),
        (r"for \((\w+) in (\d+) until (\d+)\)\s*{", r"for \1 in range(\2, \3):"),
        (r"print\((.+)\)", r"print(\1)")
    ]

    def translate_line(self, line: str) -> str:
        line = line.strip()
        for pattern, pyecv in self.rules:
            if re.match(pattern, line):
                return re.sub(pattern, pyecv, line)
        return line

    def translate(self, kotlin_code: str) -> str:
        python_code = []
        indent_level = 0
        for line in kotlin_code.splitlines():            
            indent_level -= line.count("}")
            stripped = line.strip()
            if stripped == "}":
                continue
            translated = self.translate_line(stripped)
            python_code.append("    " * indent_level + translated)    
            indent_level += line.count("{")

        return "\n".join(python_code)


kotlin_code = """

fun main() {
    val x = 5
    var y = 10
    if (x < y) {
        print("Hello world")
    } else {
        print("Bye Bye world")
    }

    for (i in 1..3) {
        print(ddd(4))
    }
}
"""

translator = KotlinToPythonTranslator()
python_code = translator.translate(kotlin_code)
print(python_code)