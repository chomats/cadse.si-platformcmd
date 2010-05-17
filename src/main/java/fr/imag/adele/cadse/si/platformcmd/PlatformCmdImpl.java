package fr.imag.adele.cadse.si.platformcmd;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import fr.imag.adele.cadse.as.platformide.IPlatformIDE;
import fr.imag.adele.cadse.as.platformide.IPlatformListener;
import fr.imag.adele.cadse.core.CadseException;
import fr.imag.adele.cadse.core.CadseRuntime;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.content.ContentItem;

public class PlatformCmdImpl implements IPlatformIDE {

	private final ReentrantLock lock = new ReentrantLock();
	private BundleContext _cxt;
	private String _location;
	
	
	public PlatformCmdImpl(BundleContext cxt) {
		this._cxt = cxt;
	}
	
	@Override
	public void activateIDE() {
	}

	@Override
	public void addListener(IPlatformListener l) {
	}

	@Override
	public void beginRule(Object rule) {
		lock.lock();
	}

	@Override
	public void copyResource(Item item, String path, URL data)
			throws CadseException {
	}

	@Override
	public void endRule(Object rule) {
		lock.unlock();
	}

	public void start() {
		_location = _cxt.getProperty("cadse.location");
		if (_location == null) {
			File dataFile = _cxt.getDataFile(".");
			if (dataFile != null)
				_location = dataFile.getAbsolutePath();
		}
		if (_location == null) {
			_location = new File(".").getAbsolutePath();
		}
	}
	
	
	@Override
	public Bundle findBundle(String symbolicName) {
		Bundle[] bundles = _cxt.getBundles();
		Bundle foundBundle = null;
		for (Bundle b : bundles) {
			if (b.getSymbolicName().equals(symbolicName)) {
				if (foundBundle == null) {
					foundBundle = b; continue;
				}
				int comp_version = b.getVersion().compareTo(foundBundle.getVersion());
				boolean bactive = b.getState() == Bundle.ACTIVE;
				boolean factive = foundBundle.getState() == Bundle.ACTIVE;
				
				if (factive) {
					if ( bactive && comp_version > 0)
						foundBundle = b; 
				} else {
					if ( bactive || comp_version > 0) {
						foundBundle = b;
					}
				}
			}
		}
		return foundBundle;
	}

	@Override
	public List<Bundle> findBundlePrefix(String prefix) {
		List<Bundle> ret = new ArrayList<Bundle>();
		Bundle[] bundles = _cxt.getBundles();
		for (Bundle b : bundles) {
			if (b.getSymbolicName().startsWith(prefix)) {
				ret.add(b);
			}
		}
		return ret ;
	}

	@Override
	public File getLocation(boolean wait) {
		return new File(_location);
	}

	@Override
	public File getLocation() {
		return new File(_location);
	}

	@Override
	public String getRessourceName(ContentItem contentItem) {
		File f = contentItem.getMainMappingContent(File.class);
		if (f != null)
			return f.getName();
		return null;
	}

	@Override
	public boolean inDevelopmentMode() {
		return false;
	}

	@Override
	public boolean isResourceStarted() {
		return true;
	}

	@Override
	public boolean isUIStarted() {
		return true;
	}

	@Override
	public void log(String type, String message, Throwable e) {
		System.out.println("["+type+"] "+message);
		if (e != null)
			e.printStackTrace();
	}

	@Override
	public void log(String type, String message, Throwable e, Item item) {
		System.out.println("["+type+"] "+message);
		if (e != null)
			e.printStackTrace();
	}

	@Override
	public void notifieChangedContent(Item item) {
	}

	@Override
	public CadseRuntime[] openDialog(boolean askToErase) {
		return new CadseRuntime[0];
	}

	@Override
	public void refresh(Item item) {
	}

	@Override
	public void removeListener(IPlatformListener l) {
	}

	@Override
	public void setItemPersistenceID(String projectName, Item item)
			throws CadseException {
	}

	@Override
	public void setReadOnly(Item item, boolean readonly) {
	}

	@Override
	public void waitUI() {
	}

}
