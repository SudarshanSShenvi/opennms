package org.opennms.features.poller.remote.gwt.client;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GWTLocationSpecificStatus implements Serializable, IsSerializable {
	private static final long serialVersionUID = 1L;

    private Integer m_id;
    private GWTLocationMonitor m_locationMonitor;
	private GWTPollResult m_pollResult;
	private GWTMonitoredService m_monitoredService;
	private Date m_pollTime;
	
    public Integer getId() {
		return m_id;
	}
	public void setId(final Integer id) {
		m_id = id;
	}
	public GWTLocationMonitor getLocationMonitor() {
		return m_locationMonitor;
	}
	public void setLocationMonitor(final GWTLocationMonitor locationMonitor) {
		m_locationMonitor = locationMonitor;
	}
	public GWTMonitoredService getMonitoredService() {
		return m_monitoredService;
	}
	public void setMonitoredService(final GWTMonitoredService monitoredService) {
		m_monitoredService = monitoredService;
	}
	public GWTPollResult getPollResult() {
		return m_pollResult;
	}
	public void setPollResult(final GWTPollResult pollResult) {
		m_pollResult = pollResult;
	}
	public Date getPollTime() {
		return m_pollTime;
	}
	public void setPollTime(final Date date) {
		m_pollTime = date;
	}

	public String toString() {
		return "GWTLocationSpecificStatus[id=" + m_id + ",locationMonitor=" + m_locationMonitor + ",monitoredService=" + m_monitoredService + ",pollResult=" + m_pollResult + "]";
	}
}
