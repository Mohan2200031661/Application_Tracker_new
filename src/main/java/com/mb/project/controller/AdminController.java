package com.mb.project.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mb.project.model.JobLink;
import com.mb.project.model.JobLink.Status;
import com.mb.project.service.JobLinkServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController 
{
	@Autowired
    private JobLinkServiceImpl jobLinkService;
	
	public AdminController(JobLinkServiceImpl jobLinkService) {
        this.jobLinkService = jobLinkService;
    }

    @GetMapping("addjob")
    public ModelAndView addjob()
    {
    	ModelAndView mv=new ModelAndView();
		mv.setViewName("addjob");
		return mv;
    }
    
    @PostMapping("insertjoblink")
    public ModelAndView insertjoblink(HttpServletRequest request)
    {
    	String companyname=request.getParameter("comna");
    	String jobdes=request.getParameter("jobdes");
    	String applicationLink = request.getParameter("applink");
    	LocalDateTime closingDateTime = LocalDateTime.parse(request.getParameter("closingdt"));
        
        
        JobLink jobLink = new JobLink();
        jobLink.setCompanyName(companyname);
        jobLink.setJobDescription(jobdes);
        jobLink.setApplicationLink(applicationLink);
        jobLink.setClosingDateTime(closingDateTime);
        jobLink.setStatus(Status.OPEN);
        
        String msg = jobLinkService.addJob(jobLink);
        
        ModelAndView mv = new ModelAndView("jobsuccess");
        mv.addObject("message", msg);
        
        return mv;
    }
    
    
    @GetMapping("viewjobs")
    public ModelAndView viewJobs() {
        ModelAndView mv = new ModelAndView("viewjobs");
        List<JobLink> jl = jobLinkService.viewalljoblinks();
        mv.addObject("jl", jl);
        mv.addObject("count", jobLinkService.jobcount());
        return mv;
    }
    
    @GetMapping("deletejob")
	public ModelAndView deletejob() 
	{
	    ModelAndView mv = new ModelAndView();
	    List<JobLink> jl = jobLinkService.viewalljoblinks();
	    long count = jobLinkService.jobcount();
	    mv.setViewName("deletejob");
	    mv.addObject("jl", jl);
	    mv.addObject("count", count);
	    return mv;
	}
    
    @GetMapping("delete")
	public String delete(@RequestParam("id")  int jid)
	{
		jobLinkService.deletejob(jid);
		return "redirect:/deletejob";
	}
    
    @GetMapping("updatejob")
    public ModelAndView updatejob(@RequestParam(value = "id", required = false) Integer jid) {
        ModelAndView mv = new ModelAndView("updatejob");
        
        if (jid != null) {
            JobLink job = jobLinkService.findById(jid);
            if (job != null) {
                mv.addObject("job", job);
            } else {
                mv.addObject("error", "Job ID not found");
            }
        }
        return mv;
    }

    @PostMapping("update")
    public ModelAndView update(HttpServletRequest request) {
        int jid = Integer.parseInt(request.getParameter("jid"));
        String companyname = request.getParameter("comna");
        String jobdes = request.getParameter("jobdes");
        String applicationLink = request.getParameter("applink");
        LocalDateTime closingDateTime = LocalDateTime.parse(request.getParameter("closingdt"));

        JobLink jobLink = new JobLink();
        jobLink.setId(jid);
        jobLink.setCompanyName(companyname);
        jobLink.setJobDescription(jobdes);
        jobLink.setApplicationLink(applicationLink);
        jobLink.setClosingDateTime(closingDateTime);
        jobLink.setStatus(Status.OPEN);

        String msg = jobLinkService.updatejob(jobLink);

        ModelAndView mv = new ModelAndView("updatejobsuccess");
        mv.addObject("message", msg);
        return mv;
    }

	
}
