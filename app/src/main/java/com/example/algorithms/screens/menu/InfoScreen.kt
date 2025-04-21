import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalUriHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("О приложении", color = Color(0xFF01579B)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color(0xFF01579B)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE1F5FE),
                    titleContentColor = Color(0xFF01579B),
                    navigationIconContentColor = Color(0xFF01579B)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(start = 16.dp, bottom = 8.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Раздел 1: О приложении
            SectionCard(
                title = "О приложении",
                icon = Icons.Default.Info
            ) {
                Paragraph(
                    """
                    Это приложение разработано для глубокого погружения в мир алгоритмов и структур данных. 
                    Здесь вы найдёте интерактивные визуализации и пошаговые объяснения.
                    Цель приложения — помочь новичкам понять базовые концепции работы алгоритмов и структур данных.
                    """
                )
                Paragraph(
                    """
                    Приложение включает следующие модули:
                    ▸ Сортировки (пузырьковая, быстрая, и др.);
                    ▸ Поисковые алгоритмы (линейный, бинарный, алгоритмы на графах);
                    ▸ Графовые алгоритмы (DFS, BFS, алгоритм Дейкстры и др.);
                    ▸ Математика (факториал, числа Фибоначчи)
                    ▸ Структуры данных (списки, стеки, очереди, деревья);
                    """
                )
            }

            // Раздел 2: Что такое алгоритм?
            SectionCard(
                title = "Что такое алгоритм?",
                icon = Icons.Default.Code
            ) {
                Paragraph(
                    """
                    Алгоритм — это чётко определённая последовательность шагов для решения конкретной задачи или класса задач. 
                    Каждый шаг должен быть однозначно трактуем и приводить к конечному результату за конечное число операций. 
                    Это фундаментальный концепт в информатике и програмировании. 
                    """
                )
                Paragraph(
                    """
                    Свойства алгоритмов:
                    ▸ Детерминированность — предсказуемость поведения;
                    ▸ Конечность — завершение за конечное время;
                    ▸ Массовость — применим к любым входным данным требуемого типа.
                    """
                )
            }

            // Раздел 3: Категории алгоритмов
            SectionCard(
                title = "Категории алгоритмов",
                icon = Icons.Default.Category
            ) {
                Paragraph(
                    """
                    ▸ Сортировки — упорядочивание элементов (быстрая, merge, heap-sort)
                    ▸ Поиск — нахождение элемента (бинарный, линейный, поиск в графе)
                    ▸ Графы — работа с вершинами и рёбрами (DFS, BFS, Дейкстра, Kruskal)
                    ▸ Математические — вычисление факториала, НОД, простых чисел
                    ▸ Строковые — поиск подстрок, анализ регулярных выражений
                    ▸ Другие — криптография, сжатие данных, машинное обучение
                    """
                )
            }

            // Раздел 4: Почему стоит изучать алгоритмы?
            SectionCard(
                title = "Почему изучать алгоритмы?",
                icon = Icons.Default.Lightbulb
            ) {
                Paragraph(
                    """
                    Изучение алгоритмов развивает критическое мышление, помогает писать эффективный код и оптимизировать ресурсы. 
                    На собеседованиях в ведущие IT-компании алгоритмы — обязательная часть тестирования навыков. 
                    Знания алгоритмов открывают двери в области Data Science, ML и крупных технологических проектов.
                    """
                )
            }

            // Раздел 5: Временная сложность
            SectionCard(
                title = "Примеры временной сложности",
                icon = Icons.Default.Timer
            ) {
                Paragraph(
                    """
                    ▸ O(1) — константное время (доступ к элементу массива)
                    ▸ O(log n) — логарифмическое (бинарный поиск в отсортированном массиве)
                    ▸ O(n) — линейное (поиск элемента в неотсортированном массиве)
                    ▸ O(n log n) — сортировки слиянием или quicksort в среднем
                    ▸ O(n²) — квадратичные (пузырьковая сортировка)
                    """
                )
            }

            // Раздел 6: Методы визуализации
            SectionCard(
                title = "Методы визуализации",
                icon = Icons.Default.RemoveRedEye
            ) {
                Paragraph(
                    """
                    В приложении используются анимации для пошагового выполнения алгоритмов:
                    ▸ Подсветка текущих элементов;
                    ▸ Движение элементов при сортировке;
                    ▸ Динамическое изменение скорости.
                    """
                )
            }

            // Раздел 7: Ресурсы для обучения
            SectionCard(
                title = "Ресурсы для обучения",
                icon = Icons.Default.School
            ) {
                InfoLink("Wikipedia — Алгоритм", "https://ru.wikipedia.org/wiki/Алгоритм")
                InfoLink("GeeksforGeeks — Fundamentals of Algorithms", "https://www.geeksforgeeks.org/fundamentals-of-algorithms/")
                InfoLink("MIT OCW 6.006", "https://ocw.mit.edu/courses/6-006-introduction-to-algorithms-spring-2020/")
                InfoLink("CLRS — Introduction to Algorithms", null)
                InfoLink("VisuAlgo", "https://visualgo.net/")
                InfoLink("AlgoAcademy Blog", "https://algocademy.com/blog/understanding-the-importance-of-algorithms-in-different-domains/")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1F9FF)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Transparent)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF0277BD),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF0277BD)
                )
            }
            content()
        }
    }
}

@Composable
fun Paragraph(text: String) {
    Text(
        text = text.trimIndent(),
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF333333),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun InfoLink(label: String, url: String?) {
    val uriHandler = LocalUriHandler.current
    if (url != null) {
        val annotatedText = buildAnnotatedString {
            pushStringAnnotation(tag = "URL", annotation = url)
            withStyle(style = SpanStyle(color = Color(0xFF01579B))) {
                append(label)
            }
            pop()
        }
        Text(
            text = annotatedText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clickable {
                    annotatedText.getStringAnnotations(tag = "URL", start = 0, end = label.length)
                        .firstOrNull()?.let { it.item.let(uriHandler::openUri) }
                }
        )
    } else {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
