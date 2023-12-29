export const getID = (videoLink: string) => {
    // TODO - Actually extract id from link
    return videoLink;
}

export const download = (downloadUrl: string, filename: string) => {
    const link = document.createElement("a");
      link.href = downloadUrl;
      link.setAttribute("download", filename);
      document.body.appendChild(link);
      link.click();
  
      // Clean up
      document.body.removeChild(link);
      window.URL.revokeObjectURL(downloadUrl);
}
  
  