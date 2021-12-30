import React from "react";

const CompareFileModal = ({ style, value, closeModal }) => {
  console.log("data-similar", value);

  return (
    <>
      {value.plagiarism && (
        <div
          className={
            "fixed p-0 top-0 left-0 right-0 bottom-0 flex justify-center w-full h-full  bg-black bg-opacity-50 antialiased overflow-x-hidden overflow-y-auto " +
            (style ? "opacity-1 visible z-10" : "opacity-0 invisible z-0")
          }
          style={{ transition: "all 0.4s" }}
        >
          <div className="my-10 mx-auto w-auto relative lg:max-w-xl lg:min-w-1/2">
            <div className="border-gray-300 shadow-xl box-border w-full bg-white p-6">
              <div className="grid grid-cols-2 mb-5 gap-1">
                <div className="bg-green-400 text-center font-semibold py-2">
                  Target Document
                </div>
                <div className="bg-red-500 text-center font-semibold py-2">
                  Matching Comparison Text
                </div>
              </div>
              {value.plagiarism.length > 0 &&
                value.plagiarism.map((data, index) => {
                  const arrTarget = data.tokenizerPlagiarism.sort(
                    (a, b) => a.startTarget - b.startTarget,
                    0
                  );
                  const arrMatching = data.tokenizerPlagiarism.sort(
                    (a, b) => a.startMatching - b.startMatching,
                    0
                  );
                  console.log("arrTarget", arrTarget);
                  return (
                    <div key={index} className="grid grid-cols-2 mb-4 gap-1">
                      <div>
                        <div className="bg-green-300 text-right pr-2 bg-opacity-50 py-1 mb-2">
                          Plagiarism Rate:{" "}
                          <span className="font-semibold">{data.rate}%</span>
                        </div>
                        {data.rate === 100 ? (
                          <mark>{data.target}</mark>
                        ) : (
                          <>
                            {arrTarget.map((data1, index) => {
                              if (index > 1) {
                                if (
                                  arrTarget[index].startTarget -
                                    (arrTarget[index - 1].startTarget +
                                      arrTarget[index - 1].length) ==
                                  1
                                ) {
                                  return (
                                    <mark key={index}>
                                      {data.target.substring(
                                        data1.startTarget - 1,
                                        data1.startTarget + data1.length
                                      )}
                                    </mark>
                                  );
                                } else {
                                  <>
                                    <span>
                                      {data.target.substring(
                                        arrTarget[index - 1].startTarget +
                                          arrTarget[index - 1].length,
                                        data1.startTarget - 1
                                      )}
                                    </span>
                                    <mark>
                                      {data.target.substring(
                                        data1.startTarget,
                                        data1.startTarget + data1.length
                                      )}
                                    </mark>
                                  </>;
                                }
                              }
                              return (
                                <mark key={index}>
                                  {data.target.substring(
                                    data1.startTarget,
                                    data1.startTarget + data1.length
                                  )}
                                </mark>
                              );
                            })}
                          </>
                        )}
                      </div>
                      <div>
                        <div className="bg-red-300 pl-2 bg-opacity-50 py-1 mb-2">
                          Document similar:{" "}
                          <span className="font-semibold">
                            {value.documentId}
                          </span>
                        </div>
                        {data.rate === 100 ? (
                          <mark>{data.matching}</mark>
                        ) : (
                          <>
                            {arrMatching.map((data1, index) => {
                              if (index > 1) {
                                if (
                                  arrMatching[index].startMatching -
                                    (arrMatching[index - 1].startMatching +
                                      arrMatching[index - 1].length) ==
                                  1
                                ) {
                                  return (
                                    <mark key={index}>
                                      {data.matching.substring(
                                        data1.startMatching - 1,
                                        data1.startMatching + data1.length
                                      )}
                                    </mark>
                                  );
                                } else {
                                  <>
                                    <span>
                                      {data.matching.substring(
                                        arrMatching[index - 1].startMatching +
                                        arrMatching[index - 1].length,
                                        data1.startMatching - 1
                                      )}
                                    </span>
                                    <mark>
                                      {data.matching.substring(
                                        data1.startMatching,
                                        data1.startMatching + data1.length
                                      )}
                                    </mark>
                                  </>;
                                }
                              }
                              return (
                                <mark key={index}>
                                  {data.matching.substring(
                                    data1.startMatching,
                                    data1.startMatching + data1.length
                                  )}
                                </mark>
                              );
                            })}
                          </>
                        )}
                      </div>
                    </div>
                  );
                })}
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default CompareFileModal;
