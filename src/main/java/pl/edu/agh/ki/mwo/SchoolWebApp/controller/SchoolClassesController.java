package pl.edu.agh.ki.mwo.SchoolWebApp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.SchoolWebApp.entity.School;
import pl.edu.agh.ki.mwo.SchoolWebApp.entity.SchoolClass;
import pl.edu.agh.ki.mwo.SchoolWebApp.entity.Teacher;
import pl.edu.agh.ki.mwo.SchoolWebApp.repository.SchoolClassRepository;
import pl.edu.agh.ki.mwo.SchoolWebApp.repository.SchoolRepository;
import pl.edu.agh.ki.mwo.SchoolWebApp.repository.TeacherRepository;

//import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class SchoolClassesController {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @RequestMapping(value = "/SchoolClasses")
    public String listSchoolClass(Model model, HttpSession session) {

        model.addAttribute("schoolClasses", schoolClassRepository.findAll());

        return "schoolClassesList";
    }

    @RequestMapping(value = "/AddSchoolClass")
    public String displayAddSchoolClassForm(Model model, HttpSession session) {

        model.addAttribute("schools", schoolRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());

        return "schoolClassForm";
    }

    @RequestMapping(value = "/CreateSchoolClass", method = RequestMethod.POST)
    public String createSchoolClass(@RequestParam(value = "schoolClassStartYear", required = false) String startYear,
            @RequestParam(value = "schoolClassCurrentYear", required = false) String currentYear,
            @RequestParam(value = "schoolClassProfile", required = false) String profile,
            @RequestParam(value = "schoolClassSchool", required = false) String schoolId,
            @RequestParam(value = "schoolClassSchool", required = false) String teacherId, Model model, HttpSession session) {

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setStartYear(Integer.valueOf(startYear));
        schoolClass.setCurrentYear(Integer.valueOf(currentYear));
        schoolClass.setProfile(profile);
        School school = schoolRepository.findById(Long.valueOf(schoolId)).get();
        Teacher teacher = teacherRepository.findById(Long.valueOf(teacherId)).get();
        schoolClass.setSchool(school);
        schoolClass.setTeacher(teacher);

        schoolClassRepository.save(schoolClass);
        model.addAttribute("schoolClasses", schoolClassRepository.findAll());
        model.addAttribute("message", "Nowa klasa została dodana");

        return "schoolClassesList";
    }

    @RequestMapping(value = "/DeleteSchoolClass", method = RequestMethod.POST)
    public String deleteSchoolClass(@RequestParam(value = "schoolClassId", required = false) String schoolClassId, Model model, HttpSession session) {

        schoolClassRepository.deleteById(Long.valueOf(schoolClassId));
        model.addAttribute("schoolClasses", schoolClassRepository.findAll());
        model.addAttribute("message", "Klasa została usunięta");

        return "schoolClassesList";
    }

    @RequestMapping(value = "/ShowUpdateSchoolClassForm")
    public String showUpdateSchoolClassForm(@RequestParam(value = "schoolClassId") String schoolClassId, Model model, HttpSession session) {

        SchoolClass schoolClass = schoolClassRepository.findById(Long.valueOf(schoolClassId)).get();
        model.addAttribute("schoolClass", schoolClass);
        model.addAttribute("schools", schoolRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());

        return "schoolClassUpdateForm";
    }

    @RequestMapping(value = "/UpdateSchoolClass", method = RequestMethod.POST)
    public String updateSchoolClass(@RequestParam(value = "schoolClassStartYear", required = false) String schoolClassStartYear,
            @RequestParam(value = "schoolClassCurrentYear", required = false) String schoolClassCurrentYear,
            @RequestParam(value = "schoolClassProfile") String schoolClassProfile, @RequestParam(value = "schoolClassId") String schoolClassId,
            @RequestParam(value = "teacherId") String teacherId, @RequestParam(value = "schoolClassSchool") String schoolClassSchool, Model model,
            HttpSession session) {

        SchoolClass schoolClass = schoolClassRepository.findById(Long.valueOf(schoolClassId)).get();
        Teacher teacher = teacherRepository.findById(Long.valueOf(teacherId)).get();
        schoolClass.setStartYear(Integer.valueOf(schoolClassStartYear));
        schoolClass.setCurrentYear(Integer.valueOf(schoolClassCurrentYear));
        schoolClass.setProfile(schoolClassProfile);

        School school = schoolRepository.findById(Long.valueOf(schoolClassSchool)).get();

        schoolClass.setSchool(school);
        schoolClass.setTeacher(teacher);
        // update
        schoolClassRepository.save(schoolClass);
        model.addAttribute("schoolClasses", schoolClassRepository.findAll());
        model.addAttribute("message", "Klasa zaktualizowana");

        return "schoolClassesList";
    }

}