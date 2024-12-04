package timetable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import timetable.service.TeacherService;

import java.io.ByteArrayInputStream;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

@SpringBootTest
class TimetableApplicationTests {

    @Autowired
    TeacherService teacherService;

	@Test
	public void testGenerateWorkerReport() throws Exception {
		// Генерация отчёта
		byte[] report = teacherService.generateTeacherReport();

		// Проверка, что отчёт не пустой
		assertNotNull(report, "Создание отчета");
		assertTrue(report.length > 0, "Отчет не должен быть пустым");

		// Проверка, что документ корректно открывается как Word
		try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(report))) {
			assertNotNull(document, "Создание документа");
			assertFalse(document.getParagraphs().isEmpty(), "Наличие параграфов в отчете");
		}
	}

}
