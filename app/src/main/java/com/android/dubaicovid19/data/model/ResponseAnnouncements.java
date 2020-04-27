package com.android.dubaicovid19.data.model;

import java.util.List;

public class ResponseAnnouncements {

    private String Status;

    private List<Announcements> Announcements;

    private String Code;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public List<Announcements> getAnnouncements ()
    {
        return Announcements;
    }

    public void setAnnouncements (List<Announcements> Announcements)
    {
        this.Announcements = Announcements;
    }

    public String getCode ()
    {
        return Code;
    }

    public void setCode (String Code)
    {
        this.Code = Code;
    }
}
