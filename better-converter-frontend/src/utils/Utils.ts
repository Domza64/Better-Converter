export const getID = (videoLink: string) => {
    const videoId = videoLink.split('v=')[1]?.split('&')[0];

    return videoId;
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
  
  