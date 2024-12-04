package timetable.service;

import timetable.model.Teacher;
import timetable.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    // Получение списка всех учителей
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // Получение учителя по идентификатору
    public Teacher getTeacherById(Integer id) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        // Возвращает экземпляр учителя или null если он не был найден
        return teacherOptional.orElse(null);
    }

    // Сохраняет экземпляр учителя в БД
    public void saveTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    // Загружает учителей из Excel файла
    public void saveTeachersFromExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            // Открываем первый лист
            Sheet sheet = workbook.getSheetAt(0);
            // Проверяем каждую строку кроме первой
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропускаем заголовок

                // Создаем новый экземпляр
                Teacher teacher = new Teacher();
                // Устанавливаем фамилию, имя и отчество
                teacher.setSurname(row.getCell(0).getStringCellValue());
                teacher.setName(row.getCell(1).getStringCellValue());
                teacher.setPatronymic(row.getCell(2).getStringCellValue());

                // Сохраняем в БД
                teacherRepository.save(teacher);
            }
        }
    }

    // Создание отчета
    public byte[] generateTeacherReport() throws IOException {

        // Получение данных из БД. Ищем учителей с занятиями 27го ноября
        List<Map<String, Object>> teachers = teacherRepository.findTeachersByDay(Date.valueOf("2024-11-27"));

        // Создание Word-документа
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Заголовок
            XWPFParagraph header = document.createParagraph();
            header.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun headerRun = header.createRun();
            headerRun.setText("ФИО всех преподавателей с парами 27го ноября (запрос от " + LocalDate.now() + ")");
            headerRun.setBold(true);
            headerRun.setFontSize(14);

            // Пустая строка
            document.createParagraph();

            // Таблица
            XWPFTable table = document.createTable();

            // Центрируем таблицу
            table.setTableAlignment(TableRowAlign.CENTER);

            // Заголовки таблицы
            XWPFTableRow headerRow = table.getRow(0);
            createStyledCell(headerRow.getCell(0), "Фамилия", true);
            createStyledCell(headerRow.addNewTableCell(), "Имя", true);
            createStyledCell(headerRow.addNewTableCell(), "Отчество", true);

            // Добавление данных
            for (Map<String, Object> teacher : teachers) {
                XWPFTableRow row = table.createRow();
                createStyledCell(row.getCell(0), teacher.get("surname") != null ? teacher.get("surname").toString() : "", false);
                createStyledCell(row.getCell(1), teacher.get("name") != null ? teacher.get("name").toString() : "", false);
                createStyledCell(row.getCell(2), teacher.get("patronymic") != null ? teacher.get("patronymic").toString() : "", false);
            }

            // Сохранение документа в байтовый массив
            document.write(out);
            return out.toByteArray();
        }
    }

    // Создаёт ячейку с заданным текстом, стилем (жирный/обычный) и размером шрифта
    private void createStyledCell(XWPFTableCell cell, String text, boolean isBold) {
        XWPFParagraph paragraph = cell.getParagraphs().getFirst();
        paragraph.setAlignment(ParagraphAlignment.CENTER); // Выравнивание текста в ячейке по центру
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(isBold);
    }

    // Удаление учителя из БД
    public void deleteTeacherById(Integer id) {
        if (getTeacherById(id) != null) {
            teacherRepository.deleteById(id);
        }
    }
}
