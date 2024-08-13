package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysAnimal;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.*;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/animal")
public class SysAnimalController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysAnimalService animalService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;


    @GetMapping("/list")
    public TableDataInfo list(SysAnimal animal) {
        startPage();
        List<SysAnimal> list = animalService.selectAnimalList(animal);
        return getDataTable(list);
    }


    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAnimal animal) {
        int i = animalService.add(animal);
        return toAjax(i);
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PutMapping
    public AjaxResult update(@RequestBody SysAnimal animal) {
        int i = animalService.update(animal);
        return toAjax(i);
    }


    /**
     * 删除用户
     */
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        int i = animalService.deleteAnimalByIds(id);
        return toAjax(i);
    }


    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        AjaxResult ajax = AjaxResult.success();
        SysAnimal animal = animalService.getInfo(id);
        ajax.put("animal", animal);
        return ajax;
    }

    @Anonymous
    @PostMapping("/upload/img")
    public AjaxResult upload(@RequestParam MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            String avatar = FileUploadUtils.upload(RuoYiConfig.getProfile()+"/animal", file, MimeTypeUtils.IMAGE_EXTENSION);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("imgUrl", avatar);
            return ajax;
        }
        return error("上传图片异常，请联系管理员");
    }
}
