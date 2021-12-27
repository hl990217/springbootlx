package cn.ty.controller;

import cn.ty.pojo.Patient;
import cn.ty.service.PatientService;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @RequestMapping("/findAll")
    public String findAll(Model model, String name, @RequestParam(defaultValue = "1")int pageNum, @RequestParam(defaultValue = "4")int pageSize, HttpServletRequest req){
        PageInfo pageInfo = patientService.findAll(name, pageNum, pageSize);
        String basePath = req.getServletContext().getRealPath("/");
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("name",name);
        String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        return "/patient/patient_list";
    }

    @RequestMapping("/updateStatus")
    public String updateStatus(Patient patient){
        patientService.updateStatus(patient);
        return "redirect:/patient/findAll";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "/patient/patient_add";
    }

    @RequestMapping("/add")
    public String add(Patient patient, MultipartFile uploadFile,HttpServletRequest req) throws IOException {
        //定义文件名
        String fileName = "";
        //处理上传的头像，先判断是否上传了文件
        if (uploadFile != null && uploadFile.getSize() > 0) {
            //1.得到上传文件的名字
            String uploadFileName = uploadFile.getOriginalFilename();
            //2.截取文件扩展名
            String fix = uploadFileName.substring(uploadFileName.lastIndexOf("."));
            //3.把文件加上随机数，防止文件重复，替换-为“”（去掉-），转换为大写字母
            String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            fileName = uuid + fix;
            //4.1把照片保存到项目static中，但是会导致项目很大，不易移植
//            String basePath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();// /D:/IdeaProjects/themeleafuserdemo/target/classes/static
            //4.2把照片保存到自定义的静态路径（和项目没有关系）中，不会导致项目很大，便于移植
            String basePath = "C:/temp/images";//使用自定义的静态资源路径
            //5.解决同一文件夹中文件过多问题，细化到以年月日作为文件夹
            String datePath = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //6.创建根目录+uploads+年月日文件夹，判断路径是否存在
            File file = new File(basePath + "/uploads/" + datePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            fileName = "uploads/" + datePath + "/" + fileName;
            //7.使用 MulitpartFile 接口中方法，把上传的文件写到指定位置
            uploadFile.transferTo(new File(basePath, fileName));
        }
        patient.setPhoto(fileName);

        patientService.add(patient);
        return "redirect:/patient/findAll";
    }

    @RequestMapping("/delete")
    public String delete(int id){
        patientService.delete(id);
        return "redirect:/patient/findAll";
    }

    @RequestMapping("/findById")
    public String findById(Model model,int id){
        Patient patient = patientService.findById(id);
        model.addAttribute("patient",patient);
        return "patient/patient_update";
    }

    @RequestMapping("/update")
    public String update(Patient patient,MultipartFile uploadFile,HttpServletRequest req) throws IOException {
        //定义文件名
        String fileName = "";
        //处理上传的头像，先判断是否上传了文件
        if (uploadFile != null && uploadFile.getSize() > 0) {
            //1.得到上传文件的名字
            String uploadFileName = uploadFile.getOriginalFilename();
            //2.截取文件扩展名
            String fix = uploadFileName.substring(uploadFileName.lastIndexOf("."));
            //3.把文件加上随机数，防止文件重复，替换-为“”（去掉-），转换为大写字母
            String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            fileName = uuid + fix;
            //4.1把照片保存到项目static中，但是会导致项目很大，不易移植
//            String basePath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();// /D:/IdeaProjects/themeleafuserdemo/target/classes/static
            //4.2把照片保存到自定义的静态路径（和项目没有关系）中，不会导致项目很大，便于移植
            String basePath = "C:/temp/images";//使用自定义的静态资源路径
            //5.解决同一文件夹中文件过多问题，细化到以年月日作为文件夹
            String datePath = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //6.创建根目录+uploads+年月日文件夹，判断路径是否存在
            File file = new File(basePath + "/uploads/" + datePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            fileName = "uploads/" + datePath + "/" + fileName;
            //7.使用 MulitpartFile 接口中方法，把上传的文件写到指定位置
            uploadFile.transferTo(new File(basePath, fileName));
        }else {
            fileName = req.getParameter("oldphoto");
        }
        patient.setPhoto(fileName);

        patientService.update(patient);
        return "redirect:/patient/findAll";
    }
}
