import { useState } from "react";
import ConvertForm from "./ConvertForm";
import { download } from "../utils/Utils";
import "./convert-container.css";

function ConvertContainer() {
  const [err, setErr] = useState(false);
  const [downloadUrl, setDownloadUrl] = useState<string | undefined>(undefined);
  const [filename, setFilename] = useState<string>("unknown");
  const [converting, setConverting] = useState(false);

  const convertFinished = (url: string, filename: string) => {
    if (url) {
      setDownloadUrl(url);
      setFilename(filename);
      setConverting(false);
    } else {
      setErr(true);
      setConverting(false);
    }
  };

  const convertFailed = () => {
    setErr(true);
    handleConvertNext();
  };

  const startConverting = () => {
    setConverting(true);
    setErr(false);
  };

  const handleConvertNext = () => {
    setDownloadUrl(undefined);
    setConverting(false);
  };

  const handleDownloadButton = () => {
    if (downloadUrl) {
      download(downloadUrl, filename);
    }
    handleConvertNext();
  };

  return (
    <>
      {err && <span>Err converting video</span>}
      {downloadUrl ? (
        <div className="download-ready">
          <span>Ready for download: {filename}</span>
          <div>
            <button onClick={handleDownloadButton}>Download</button>
            <button onClick={handleConvertNext}>Convert Next</button>
          </div>
        </div>
      ) : converting ? (
        <span>Converting...</span>
      ) : (
        <ConvertForm
          convertFinished={convertFinished}
          convertFailed={convertFailed}
          startConverting={startConverting}
        />
      )}
    </>
  );
}

export default ConvertContainer;
